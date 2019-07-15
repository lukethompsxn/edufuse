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
    free(fileInfo); //freeing this may cause seg fault if we call more send methods with it

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

char *stringify_fusefileinfo(struct fuse_file_info *fi) {
    char *json = mkjson(MKJSON_OBJ, 10,
                        MKJSON_INT, "flags", fi->flags,
                        MKJSON_INT, "writepage", fi->writepage,
                        MKJSON_BOOL, "direct_io", fi->direct_io,
                        MKJSON_BOOL, "keep_cache", fi->keep_cache,
                        MKJSON_BOOL, "flush", fi->flush,
                        MKJSON_BOOL, "nonseekable", fi->nonseekable,
                        MKJSON_BOOL, "flock_release", fi->flock_release,
                        MKJSON_INT, "padding", fi->padding,
                        MKJSON_LLINT, "fh", fi->fh,
                        MKJSON_LLINT, "lock_owner", fi->lock_owner);

    return json;
}

char *stringify_fusefileinfo_with_buf_size_off(struct fuse_file_info *fi, char *buf, size_t size, off_t off) {
    char *json = mkjson(MKJSON_OBJ, 4,
                        MKJSON_JSON, "fi", mkjson(MKJSON_OBJ, 10,
                                MKJSON_INT, "flags", fi->flags,
                                MKJSON_INT, "writepage", fi->writepage,
                                MKJSON_BOOL, "direct_io", fi->direct_io,
                                MKJSON_BOOL, "keep_cache", fi->keep_cache,
                                MKJSON_BOOL, "flush", fi->flush,
                                MKJSON_BOOL, "nonseekable", fi->nonseekable,
                                MKJSON_BOOL, "flock_release", fi->flock_release,
                                MKJSON_INT, "padding", fi->padding,
                                MKJSON_LLINT, "fh", fi->fh,
                                MKJSON_LLINT, "lock_owner", fi->lock_owner),
                        MKJSON_STRING, "buf", buf,
                        MKJSON_LLINT, "size", size,
                        MKJSON_LLINT, "off", off);

    return json;
}

char *stringify_fusefileinfo_with_datasync(struct fuse_file_info *fi, int datasync) {
    char *json = mkjson(MKJSON_OBJ, 2,
                        MKJSON_JSON, "fi", mkjson(MKJSON_OBJ, 10,
                                MKJSON_INT, "flags", fi->flags,
                                MKJSON_INT, "writepage", fi->writepage,
                                MKJSON_BOOL, "direct_io", fi->direct_io,
                                MKJSON_BOOL, "keep_cache", fi->keep_cache,
                                MKJSON_BOOL, "flush", fi->flush,
                                MKJSON_BOOL, "nonseekable", fi->nonseekable,
                                MKJSON_BOOL, "flock_release", fi->flock_release,
                                MKJSON_INT, "padding", fi->padding,
                                MKJSON_LLINT, "fh", fi->fh,
                                MKJSON_LLINT, "lock_owner", fi->lock_owner),
                        MKJSON_INT, "datasync", datasync);

    return json;
}

char *stringify_fusefileinfo_with_mode(struct fuse_file_info *fi, int mode) {
    char *json = mkjson(MKJSON_OBJ, 2,
                        MKJSON_JSON, "fi", mkjson(MKJSON_OBJ, 10,
                                MKJSON_INT, "flags", fi->flags,
                                MKJSON_INT, "writepage", fi->writepage,
                                MKJSON_BOOL, "direct_io", fi->direct_io,
                                MKJSON_BOOL, "keep_cache", fi->keep_cache,
                                MKJSON_BOOL, "flush", fi->flush,
                                MKJSON_BOOL, "nonseekable", fi->nonseekable,
                                MKJSON_BOOL, "flock_release", fi->flock_release,
                                MKJSON_INT, "padding", fi->padding,
                                MKJSON_LLINT, "fh", fi->fh,
                                MKJSON_LLINT, "lock_owner", fi->lock_owner),
                        MKJSON_INT, "mode", mode);

    return json;
}

char *stringify_fusefileinfo_with_flock_cmd(struct fuse_file_info *fi, struct flock *lock, int cmd) {
    char *json = mkjson(MKJSON_OBJ, 3,
                        MKJSON_JSON, "fi", mkjson(MKJSON_OBJ, 10,
                                MKJSON_INT, "flags", fi->flags,
                                MKJSON_INT, "writepage", fi->writepage,
                                MKJSON_BOOL, "direct_io", fi->direct_io,
                                MKJSON_BOOL, "keep_cache", fi->keep_cache,
                                MKJSON_BOOL, "flush", fi->flush,
                                MKJSON_BOOL, "nonseekable", fi->nonseekable,
                                MKJSON_BOOL, "flock_release", fi->flock_release,
                                MKJSON_INT, "padding", fi->padding,
                                MKJSON_LLINT, "fh", fi->fh,
                                MKJSON_LLINT, "lock_owner", fi->lock_owner),
                        MKJSON_JSON, "lock", mkjson(MKJSON_OBJ, 4,
                                MKJSON_INT, "type", lock->l_type,
                                MKJSON_INT, "whence", lock->l_whence,
                                MKJSON_LLINT, "start", lock->l_start,
                                MKJSON_LLINT, "len", lock->l_len,
                                MKJSON_LLINT, "pid", lock->l_pid),
                        MKJSON_INT, "cmd", cmd);

    return json;
}

char *stringify_statvfs(struct statvfs *buf) {
    char *json = mkjson(MKJSON_OBJ, 10,
                        MKJSON_LLINT, "bsize", buf->f_bsize,
                        MKJSON_LLINT, "frsize", buf->f_frsize,
                        MKJSON_LLINT, "blocks", buf->f_blocks,
                        MKJSON_LLINT, "bavail", buf->f_bavail,
                        MKJSON_LLINT, "files", buf->f_files,
                        MKJSON_LLINT, "ffree", buf->f_ffree,
                        MKJSON_LLINT, "favail", buf->f_favail,
                        MKJSON_LLINT, "fsid", buf->f_fsid,
                        MKJSON_LLINT, "flag", buf->f_flag,
                        MKJSON_LLINT, "namemax", buf->f_namemax);

    return json;
}

char *stringify_fuseconninfo(struct fuse_conn_info *conn) {
    char *json = mkjson(MKJSON_OBJ, 8,
                        MKJSON_INT, "proto_major", conn->proto_major,
                        MKJSON_INT, "proto_minor", conn->proto_minor,
                        MKJSON_INT, "async_read", conn->async_read,
                        MKJSON_INT, "max_write", conn->max_write,
                        MKJSON_INT, "max_readahead", conn->max_readahead,
                        MKJSON_INT, "capable", conn->capable,
                        MKJSON_INT, "want", conn->want,
                        MKJSON_INT, "congestion_threshold", conn->congestion_threshold);

    return json;
}

char *stringify_tv(struct timespec tv[2]) {
    char *json = mkjson(MKJSON_OBJ, 1,
                        MKJSON_JSON_FREE, "object", mkjson(MKJSON_ARR, 2,
                                MKJSON_JSON, mkjson(MKJSON_OBJ, 2,
                                        MKJSON_LLINT, "sec", tv[0].tv_sec,
                                        MKJSON_LLINT, "nsec", tv[0].tv_nsec),
                                MKJSON_JSON, mkjson(MKJSON_OBJ, 2,
                                        MKJSON_LLINT, "sec", tv[1].tv_sec,
                                        MKJSON_LLINT, "nsec", tv[1].tv_nsec)));

    return json;
}



