#define FUSE_USE_VERSION 31

#include "edufuse.h"
#include "edufuse_visualiser.h"
#include <fuse.h>
#include <string.h>
#include <errno.h>
#include <stdio.h>
#include <stdlib.h>

static struct fuse_operations *registered_operations;
int is_visualised;

/** Get file attributes */
static int edufuse_getattr(const char *path, struct stat *stbuf) {
    send_data("test getattr called");
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

/** Get extended attributes */
static int edufuse_getxattr(const char *path, const char *name, char *value, size_t size) {
    return registered_operations->getxattr(path, name, value, size);
}

/** List extended attributes */
static int edufuse_listxattr(const char *path, char *list, size_t size) {
    return registered_operations->listxattr(path, list, size);
}

/** Remove extended attributes */
static int edufuse_removexattr(const char *path, const char *name) {
    return registered_operations->removexattr(path, name);
}

/** Open directory */
static int edufuse_opendir(const char *path, struct fuse_file_info *fi) {
    return registered_operations->opendir(path, fi);
}

/** Read directory */
static int edufuse_readdir(const char *path, void *buf, fuse_fill_dir_t filler, off_t off, struct fuse_file_info *fi) {
    return registered_operations->readdir(path, buf, filler, off, fi);
}

/** Release directory */
static int edufuse_releasedir(const char *path, struct fuse_file_info *fi) {
    return registered_operations->releasedir(path, fi);
}

/** Synchronize directory contents */
static int edufuse_fsyncdir(const char *path, int datasync, struct fuse_file_info *fi) {
    return registered_operations->fsyncdir(path, datasync, fi);
}

/** Initialize filesystem */
static void *edufuse_init(struct fuse_conn_info *conn) {
    return registered_operations->init(conn);
}

/**
 * Clean up filesystem */
static void edufuse_destroy(void *data) {
    registered_operations->destroy(data);
}

/** Check file access permissions */
static int edufuse_access(const char *path, int mask) {
    return registered_operations->access(path, mask);
}

/**
 * Create and open a file */
static int edufuse_create(const char *path, mode_t mode, struct fuse_file_info *fi) {
    return registered_operations->create(path, mode, fi);
}

/** Change the size of an open file */
static int edufuse_ftruncate(const char *path, off_t size, struct fuse_file_info *fi) {
    return registered_operations->ftruncate(path, size, fi);
}

/** Get attributes from an open file */
static int edufuse_fgetattr(const char *path, struct stat *buf, struct fuse_file_info *fi) {
    return registered_operations->fgetattr(path, buf, fi);
}

/** Perform POSIX file locking operation */
static int edufuse_lock(const char *path, struct fuse_file_info *fi, int cmd, struct flock *lock) {
    return registered_operations->lock(path, fi, cmd, lock);
}

/** Change the access and modification times of a file with */
static int edufuse_utimens(const char *path, const struct timespec tv[2]) {
    return registered_operations->utimens(path, tv);
}

/** Map block index within file to block index within device */
static int edufuse_bmap(const char *path, size_t blocksize, uint64_t *idx) {
    return registered_operations->bmap(path, blocksize, idx);
}

/** Ioctl */
static int edufuse_ioctl(const char *path, int cmd, void *arg, struct fuse_file_info *fi, unsigned int flags, void *data) {
    return registered_operations->ioctl(path, cmd, arg, fi, flags, data);
}

/** Poll for IO readiness events */
static int edufuse_poll(const char *path, struct fuse_file_info *fi, struct fuse_pollhandle *ph, unsigned *reventsp) {
    return registered_operations->poll(path, fi, ph, reventsp);
}

/** Register implemented methods with eduFUSE */
int edufuse_register(int argc, char *argv[], struct fuse_operations *edufuse_operations, int visualise) {
    registered_operations = malloc(sizeof(struct fuse_operations));
    memcpy(registered_operations, edufuse_operations, sizeof(struct fuse_operations));
    is_visualised = visualise;

    if (is_visualised) {
        init_visualiser();
        atexit(destroy_visualiser);
    }

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

    if (edufuse_operations->getxattr != NULL) {
        edufuse_operations->getxattr = edufuse_getxattr;
    }

    if (edufuse_operations->listxattr != NULL) {
        edufuse_operations->listxattr = edufuse_listxattr;
    }

    if (edufuse_operations->removexattr != NULL) {
        edufuse_operations->removexattr = edufuse_removexattr;
    }

    if (edufuse_operations->opendir != NULL) {
        edufuse_operations->opendir = edufuse_opendir;
    }

    if (edufuse_operations->readdir != NULL) {
        edufuse_operations->readdir = edufuse_readdir;
    }

    if (edufuse_operations->releasedir != NULL) {
        edufuse_operations->releasedir = edufuse_releasedir;
    }

    if (edufuse_operations->fsyncdir != NULL) {
        edufuse_operations->fsyncdir = edufuse_fsyncdir;
    }

    if (edufuse_operations->init != NULL) {
        edufuse_operations->init = edufuse_init;
    }

    if (edufuse_operations->destroy != NULL) {
        edufuse_operations->destroy = edufuse_destroy;
    }

    if (edufuse_operations->access != NULL) {
        edufuse_operations->access = edufuse_access;
    }

    if (edufuse_operations->create != NULL) {
        edufuse_operations->create = edufuse_create;
    }

    if (edufuse_operations->ftruncate != NULL) {
        edufuse_operations->ftruncate = edufuse_ftruncate;
    }

    if (edufuse_operations->fgetattr != NULL) {
        edufuse_operations->fgetattr = edufuse_fgetattr;
    }

    if (edufuse_operations->lock != NULL) {
        edufuse_operations->lock = edufuse_lock;
    }

    if (edufuse_operations->utimens != NULL) {
        edufuse_operations->utimens = edufuse_utimens;
    }

    if (edufuse_operations->bmap != NULL) {
        edufuse_operations->bmap = edufuse_bmap;
    }

    if (edufuse_operations->ioctl != NULL) {
        edufuse_operations->ioctl = edufuse_ioctl;
    }

    if (edufuse_operations->poll != NULL) {
        edufuse_operations->poll = edufuse_poll;
    }

    return fuse_main(argc, argv, edufuse_operations, NULL);
}
