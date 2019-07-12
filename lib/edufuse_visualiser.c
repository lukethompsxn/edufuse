#include "edufuse_visualiser.h"
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <errno.h>
#include <string.h>

#include <netdb.h>
#include <arpa/inet.h>
#include "../ext/mkjson/mkjson.h"

#define HOST "127.0.0.1"
#define PORT 8081

int sockfd;
struct sockaddr_in servaddr;
int attempt = 1;

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
