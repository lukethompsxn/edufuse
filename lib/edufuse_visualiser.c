#include "edufuse_visualiser.h"
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <errno.h>
#include <string.h>

#include <netdb.h>
#include <arpa/inet.h>
#include <bits/stat.h>
#include "../ext/mkjson/mkjson.h"

#define HOST "127.0.0.1"
#define PORT 8081

int sockfd;
struct sockaddr_in servaddr;
int attempt = 1;

/*
 *
 *
 * Setup Methods
 *
 *
 */

void connect_to_socket() {
    if (connect(sockfd, (struct sockaddr *) &servaddr, sizeof(servaddr)) != 0) {
        if (attempt < 3) {
            printf("%s%i%s","Socket not ready, sleeping for 2 seconds then retrying... (attempt ", attempt,"/2)\n");
            attempt = attempt + 1;
            usleep(2000000);
            connect_to_socket();
        } else {
            printf("Failed to connect to the socket.... exiting....\n");
            exit(0);
        }
    } else {
        printf("Successfully connected to socket\n");
    }
}

int init_visualiser() {
//    system("chmod +x ../../gui/launch.sh");
//    int pid = fork();
//    if (pid == 0) {
//        system("../../gui/launch.sh");
//        exit(0);
//    }

    if ((sockfd = socket(AF_INET, SOCK_STREAM, 0)) == -1) {
        printf("Socket creation failed... exiting...\n");
        exit(0);
    }

    printf("Socket was successfully created.\n");
    bzero(&servaddr, sizeof(servaddr));

    servaddr.sin_family = AF_INET;
    servaddr.sin_addr.s_addr = inet_addr(HOST);
    servaddr.sin_port = htons(PORT);

    connect_to_socket();

    return 0;
}

void destroy_visualiser() {

}

//todo make methods properly, this was just for testing socket communication
int send_data(char *str) {
    char *json = mkjson( MKJSON_OBJ, 2,
                         MKJSON_STRING,    "this",    "is really simple!",
                         MKJSON_INT,       "myint",   42);

    return send(sockfd, json, strlen(json), 0);
}

/*
 *
 *
 * Communication Methods
 *
 *
 */

int send_log(char *syscall, char *file, char *fileInfo) {
    char *msg = mkjson( MKJSON_OBJ, 4,
                         MKJSON_STRING, "type", "LOG",
                         MKJSON_STRING, "syscall", syscall,
                         MKJSON_STRING, "file", file,
                         MKJSON_JSON, "fileInfo", fileInfo);

    int result = send(sockfd, msg, strlen(msg), 0);
    free(msg);
    free(fileInfo);

    return result;
}

/*
 *
 *
 * Stringify Methods
 *
 *
 */
char *stringify_stat(struct stat *stbuf) {
    char *json = mkjson(MKJSON_OBJ, 11,
                        MKJSON_LLINT, "dev", stbuf->st_dev,
                        MKJSON_LLINT, "ino", stbuf->st_ino,
                        MKJSON_INT, "mode", stbuf->st_mode,
                        MKJSON_LLINT, "nlink", stbuf->st_nlink,
                        MKJSON_INT, "uid", stbuf->st_uid,
                        MKJSON_INT, "gid", stbuf->st_gid,
                        MKJSON_INT, "pad0", stbuf->__pad0,
                        MKJSON_LLINT, "rdev", stbuf->st_rdev,
                        MKJSON_LLINT, "size", stbuf->st_size,
                        MKJSON_INT, "blksize", stbuf->st_blksize,
                        MKJSON_LLINT, "blocks", stbuf->st_blocks);

    return json;
}

