#define FUSE_USE_VERSION 31

#include "../lib/edufuse.h"
#include <fuse.h>
#include <string.h>
#include <errno.h>
#include <stdio.h>

static const char *filepath = "/file";
static const char *filename = "file";
static const char *filecontent = "I'm the content of the only file available there\n";

static int efs_getattr(const char *path, struct stat *stbuf) {
    memset(stbuf, 0, sizeof(struct stat));

    if (strcmp(path, "/") == 0) {
        stbuf->st_mode = S_IFDIR | 0755;
        stbuf->st_nlink = 2;
        return 0;
    }

    if (strcmp(path, filepath) == 0) {
        stbuf->st_mode = S_IFREG | 0777;
        stbuf->st_nlink = 1;
        stbuf->st_size = strlen(filecontent);
        return 0;
    }

    return -ENOENT;
}

static int efs_readdir(const char *path, void *buf, fuse_fill_dir_t filler,
                           off_t offset, struct fuse_file_info *fi) {
    (void) offset;
    (void) fi;

    filler(buf, ".", NULL, 0);
    filler(buf, "..", NULL, 0);

    filler(buf, filename, NULL, 0);

    return 0;
}

static int efs_open(const char *path, struct fuse_file_info *fi) {
    return 0;
}

static int efs_read(const char *path, char *buf, size_t size, off_t offset,
                        struct fuse_file_info *fi) {
    if (strcmp(path, filepath) == 0) {
        size_t len = strlen(filecontent);
        if (offset >= len) {
            return 0;
        }

        if (offset + size > len) {
            memcpy(buf, filecontent + offset, len - offset);
            return len - offset;
        }

        memcpy(buf, filecontent + offset, size);
        return size;
    }

    return -ENOENT;
}

static struct fuse_operations efs_operations = {
        .getattr = efs_getattr,
        .open = efs_open,
        .read = efs_read,
        .readdir = efs_readdir,
};

int main(int argc, char *argv[])
{
    //todo remove hardcoded is visualised parameter
    return edufuse_register(argc, argv, &efs_operations, 1);
}
