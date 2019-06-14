#define FUSE_USE_VERSION 31

#include "edufuse.h"
#include <fuse.h>
#include <string.h>
#include <errno.h>
#include <stdio.h>
#include <stdlib.h>

static struct fuse_operations *registered_operations;

static int edufuse_getattr(const char *path, struct stat *stbuf) {
    return registered_operations->getattr(path, stbuf);
}

static int edufuse_readdir(const char *path, void *buf, fuse_fill_dir_t filler,
    off_t offset, struct fuse_file_info *fi) {
    return registered_operations->readdir(path, buf, filler, offset, fi);
}

static int edufuse_open(const char *path, struct fuse_file_info *fi) {
    return registered_operations->open(path, fi);
}

static int edufuse_read(const char *path, char *buf, size_t size, off_t offset,
    struct fuse_file_info *fi) {
    return registered_operations->read(path, buf, size, offset, fi);
}

int edufuse_register(int argc, char *argv[], struct fuse_operations *edufuse_operations, int size) {
    registered_operations = malloc(size);
    memcpy(registered_operations, edufuse_operations, size);



    // todo may be able to loop through these using memory additions
    if (edufuse_operations->getattr != NULL) {
        edufuse_operations->getattr = edufuse_getattr;
    }

    if (edufuse_operations->readdir != NULL) {
        edufuse_operations->readdir = edufuse_readdir;
    }

    if (edufuse_operations->open != NULL) {
        edufuse_operations->open = edufuse_open;
    }

    if (edufuse_operations->read != NULL) {
        edufuse_operations->read = edufuse_read;
    }

    return fuse_main(argc, argv, edufuse_operations, NULL);
}
