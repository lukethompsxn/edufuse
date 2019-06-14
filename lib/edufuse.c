#define FUSE_USE_VERSION 31

#include "edufuse.h"
#include <fuse.h>
#include <string.h>
#include <errno.h>
#include <stdio.h>
#include <stdlib.h>

static struct fuse_operations *registered_operations;

/** Get file attributes */
static int edufuse_getattr(const char *path, struct stat *stbuf) {
    return registered_operations->getattr(path, stbuf);
}

/** Read the target of a symbolic link */
static int edufuse_readlink(const char *path, char *buf, size_t len) {
    return registered_operations->readlink(path, buf, len);
}

/** Create a file node */
static int edufuse_mknod(const char *path, mode_t mode, dev_t rdev) {
    return registered_operations->mknod(path, mode, rdev);
}

/** Create a directory */
static int edufuse_mkdir(const char *path, mode_t mode) {
    return registered_operations->mkdir(path, mode);
}

/** Remove a file */
static int edufuse_unlink(const char *path) {
    return registered_operations->unlink(path);
}

/** Remove a directory */
static int edufuse_rmdir(const char *path) {
    return registered_operations->rmdir(path);
}

/** Create a symbolic link */
static int edufuse_symlink(const char *linkname, const char *path) {
    return registered_operations->symlink(linkname, path);
}

/** Rename a file */
static int edufuse_rename(const char *oldpath, const char *newpath) {
    return registered_operations->rename(oldpath, newpath);
}

/** Create a hard link to a file */
static int edufuse_link(const char *oldpath, const char *newpath) {
    return registered_operations->link(oldpath, newpath);
}

/** Change the permission bits of a file */
static int edufuse_chmod(const char *path, mode_t mode) {
    return registered_operations->chmod(path, mode);
}

/** Change the owner and group of a file */
static int edufuse_chown(const char *path, uid_t uid, gid_t gid) {
    return registered_operations->chown(path, uid, gid);
}

/** Change the size of a file */
static int edufuse_truncate(const char *path, off_t size) {
    return registered_operations->truncate(path, size);
}

/** File open operation */
static int edufuse_open(const char *path, struct fuse_file_info *fi) {
    return registered_operations->open(path, fi);
}

/** Read data from an open file */
static int edufuse_read(const char *path, char *buf, size_t size, off_t off, struct fuse_file_info *fi) {
    return registered_operations->read(path, buf, size, off, fi);
}

/** Write data to an open file */
static int edufuse_write(const char *path, const char *buf, size_t size, off_t off, struct fuse_file_info *fi) {
    return registered_operations->write(path, buf, size, off, fi);
}

/** Get file system statistics */
static int edufuse_statfs(const char *path, struct statvfs *buf) {
    return registered_operations->statfs(path, buf);
}

/** Possibly flush cached data */
static int edufuse_flush(const char *path, struct fuse_file_info *fi) {
    return registered_operations->flush(path, fi);
}

/** Release an open file */
static int edufuse_release(const char *path, struct fuse_file_info *fi) {
    return registered_operations->release(path, fi);
}

/** Synchronize file contents */
static int edufuse_fsync(const char *path, int datasync, struct fuse_file_info *fi) {
    return registered_operations->fsync(path, datasync, fi);
}

/** Set extended attributes */
static int edufuse_setxattr(const char *path, const char *name, const char *value, size_t size, int flags) {
    return registered_operations->setxattr(path, name, value, size, flags);
}

/** Register implemented methods with eduFUSE */
int edufuse_register(int argc, char *argv[], struct fuse_operations *edufuse_operations, int size) {
    registered_operations = malloc(size);
    memcpy(registered_operations, edufuse_operations, size);

    if (edufuse_operations->getattr != NULL) {
        edufuse_operations->getattr = edufuse_getattr;
    }

    if (edufuse_operations->readlink != NULL) {
        edufuse_operations->readlink = edufuse_readlink;
    }

    if (edufuse_operations->mknod != NULL) {
        edufuse_operations->mknod = edufuse_mknod;
    }

    if (edufuse_operations->mkdir != NULL) {
        edufuse_operations->mkdir = edufuse_mkdir;
    }

    if (edufuse_operations->unlink != NULL) {
        edufuse_operations->unlink = edufuse_unlink;
    }

    if (edufuse_operations->rmdir != NULL) {
        edufuse_operations->rmdir = edufuse_rmdir;
    }

    if (edufuse_operations->symlink != NULL) {
        edufuse_operations->symlink = edufuse_symlink;
    }

    if (edufuse_operations->rename != NULL) {
        edufuse_operations->rename = edufuse_rename;
    }

    if (edufuse_operations->link != NULL) {
        edufuse_operations->link = edufuse_link;
    }

    if (edufuse_operations->chmod != NULL) {
        edufuse_operations->chmod = edufuse_chmod;
    }

    if (edufuse_operations->chown != NULL) {
        edufuse_operations->chown = edufuse_chown;
    }

    if (edufuse_operations->truncate != NULL) {
        edufuse_operations->truncate = edufuse_truncate;
    }

    if (edufuse_operations->open != NULL) {
        edufuse_operations->open = edufuse_open;
    }

    if (edufuse_operations->read != NULL) {
        edufuse_operations->read = edufuse_read;
    }

    if (edufuse_operations->write != NULL) {
        edufuse_operations->write = edufuse_write;
    }

    if (edufuse_operations->statfs != NULL) {
        edufuse_operations->statfs = edufuse_statfs;
    }

    if (edufuse_operations->flush != NULL) {
        edufuse_operations->flush = edufuse_flush;
    }

    if (edufuse_operations->release != NULL) {
        edufuse_operations->release = edufuse_release;
    }

    if (edufuse_operations->fsync != NULL) {
        edufuse_operations->fsync = edufuse_fsync;
    }

    if (edufuse_operations->setxattr != NULL) {
        edufuse_operations->setxattr = edufuse_setxattr;
    }

    return fuse_main(argc, argv, edufuse_operations, NULL);
}
