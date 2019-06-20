#define FUSE_USE_VERSION 31

#include <jni.h>
#include <stdio.h>
#include "util_FUSELink.h"
#include "../lib/edufuse.h"
#include <fuse.h>
#include <string.h>
#include <errno.h>
#include <stdlib.h>

static struct fuse_operations *registered_operations;

/**
 *
 * eduFUSE Functions
 *
 */

/** Get file attributes */
static int jef_getattr(const char *path, struct stat *stbuf) {
    return registered_operations->getattr(path, stbuf);
}

/** Read the target of a symbolic link */
static int jef_readlink(const char *path, char *buf, size_t len) {
    return registered_operations->readlink(path, buf, len);
}

/** Create a file node */
static int jef_mknod(const char *path, mode_t mode, dev_t rdev) {
    return registered_operations->mknod(path, mode, rdev);
}

/** Create a directory */
static int jef_mkdir(const char *path, mode_t mode) {
    return registered_operations->mkdir(path, mode);
}

/** Remove a file */
static int jef_unlink(const char *path) {
    return registered_operations->unlink(path);
}

/** Remove a directory */
static int jef_rmdir(const char *path) {
    return registered_operations->rmdir(path);
}

/** Create a symbolic link */
static int jef_symlink(const char *linkname, const char *path) {
    return registered_operations->symlink(linkname, path);
}

/** Rename a file */
static int jef_rename(const char *oldpath, const char *newpath) {
    return registered_operations->rename(oldpath, newpath);
}

/** Create a hard link to a file */
static int jef_link(const char *oldpath, const char *newpath) {
    return registered_operations->link(oldpath, newpath);
}

/** Change the permission bits of a file */
static int jef_chmod(const char *path, mode_t mode) {
    return registered_operations->chmod(path, mode);
}

/** Change the owner and group of a file */
static int jef_chown(const char *path, uid_t uid, gid_t gid) {
    return registered_operations->chown(path, uid, gid);
}

/** Change the size of a file */
static int jef_truncate(const char *path, off_t size) {
    return registered_operations->truncate(path, size);
}

/** File open operation */
static int jef_open(const char *path, struct fuse_file_info *fi) {
    return registered_operations->open(path, fi);
}

/** Read data from an open file */
static int jef_read(const char *path, char *buf, size_t size, off_t off, struct fuse_file_info *fi) {
    return registered_operations->read(path, buf, size, off, fi);
}

/** Write data to an open file */
static int jef_write(const char *path, const char *buf, size_t size, off_t off, struct fuse_file_info *fi) {
    return registered_operations->write(path, buf, size, off, fi);
}

/** Get file system statistics */
static int jef_statfs(const char *path, struct statvfs *buf) {
    return registered_operations->statfs(path, buf);
}

/** Possibly flush cached data */
static int jef_flush(const char *path, struct fuse_file_info *fi) {
    return registered_operations->flush(path, fi);
}

/** Release an open file */
static int jef_release(const char *path, struct fuse_file_info *fi) {
    return registered_operations->release(path, fi);
}

/** Synchronize file contents */
static int jef_fsync(const char *path, int datasync, struct fuse_file_info *fi) {
    return registered_operations->fsync(path, datasync, fi);
}

/** Set extended attributes */
static int jef_setxattr(const char *path, const char *name, const char *value, size_t size, int flags) {
    return registered_operations->setxattr(path, name, value, size, flags);
}

/** Get extended attributes */
static int jef_getxattr(const char *path, const char *name, char *value, size_t size) {
    return registered_operations->getxattr(path, name, value, size);
}

/** List extended attributes */
static int jef_listxattr(const char *path, char *list, size_t size) {
    return registered_operations->listxattr(path, list, size);
}

/** Remove extended attributes */
static int jef_removexattr(const char *path, const char *name) {
    return registered_operations->removexattr(path, name);
}

/** Open directory */
static int jef_opendir(const char *path, struct fuse_file_info *fi) {
    return registered_operations->opendir(path, fi);
}

/** Read directory */
static int jef_readdir(const char *path, void *buf, fuse_fill_dir_t filler, off_t off, struct fuse_file_info *fi) {
    return registered_operations->readdir(path, buf, filler, off, fi);
}

/** Release directory */
static int jef_releasedir(const char *path, struct fuse_file_info *fi) {
    return registered_operations->releasedir(path, fi);
}

/** Synchronize directory contents */
static int jef_fsyncdir(const char *path, int datasync, struct fuse_file_info *fi) {
    return registered_operations->fsyncdir(path, datasync, fi);
}

/** Initialize filesystem */
static void *jef_init(struct fuse_conn_info *conn) {
    return registered_operations->init(conn);
}

/**
 * Clean up filesystem */
static void jef_destroy(void *data) {
    registered_operations->destroy(data);
}

/** Check file access permissions */
static int jef_access(const char *path, int mask) {
    return registered_operations->access(path, mask);
}

/**
 * Create and open a file */
static int jef_create(const char *path, mode_t mode, struct fuse_file_info *fi) {
    return registered_operations->create(path, mode, fi);
}

/** Change the size of an open file */
static int jef_ftruncate(const char *path, off_t size, struct fuse_file_info *fi) {
    return registered_operations->ftruncate(path, size, fi);
}

/** Get attributes from an open file */
static int jef_fgetattr(const char *path, struct stat *buf, struct fuse_file_info *fi) {
    return registered_operations->fgetattr(path, buf, fi);
}

/** Perform POSIX file locking operation */
static int jef_lock(const char *path, struct fuse_file_info *fi, int cmd, struct flock *lock) {
    return registered_operations->lock(path, fi, cmd, lock);
}

/** Change the access and modification times of a file with */
static int jef_utimens(const char *path, const struct timespec tv[2]) {
    return registered_operations->utimens(path, tv);
}

/** Map block index within file to block index within device */
static int jef_bmap(const char *path, size_t blocksize, uint64_t *idx) {
    return registered_operations->bmap(path, blocksize, idx);
}

/** Ioctl */
static int jef_ioctl(const char *path, int cmd, void *arg, struct fuse_file_info *fi, unsigned int flags, void *data) {
    return registered_operations->ioctl(path, cmd, arg, fi, flags, data);
}

/** Poll for IO readiness events */
static int jef_poll(const char *path, struct fuse_file_info *fi, struct fuse_pollhandle *ph, unsigned *reventsp) {
    return registered_operations->poll(path, fi, ph, reventsp);
}


/**
 *
 * Java Native Functions
 *
 */

 static char *operations_contains(JNIEnv *env, jobject operations_map, jmethodID contains_m, char *str) {
     char *data= (char*)malloc(16);
     strcpy(data, "getattr");
     jstring jstr = (*env)->NewStringUTF(env, data);
     char *bool = (char *)(*env)->CallObjectMethod(env, operations_map, contains_m, jstr);
     free(data);
     return bool;
 }

JNIEXPORT jint JNICALL Java_util_FUSELink_registerOperations(JNIEnv *env, jobject this, jobject operations_map)
{
    registered_operations = malloc(sizeof(struct fuse_operations));
    jclass function_map_c = (*env)->FindClass(env, "util/FunctionMap");
    jmethodID contains_m = (*env)->GetMethodID(env, function_map_c, "contains", "(Ljava/lang/String;)Z");

    if (operations_contains(env, operations_map, contains_m, "getattr")) {
        registered_operations->getattr = jef_getattr;
    }

//    if (jef_operations->readlink != NULL) {
//        jef_operations->readlink = jef_readlink;
//    }
//
//    if (jef_operations->mknod != NULL) {
//        jef_operations->mknod = jef_mknod;
//    }
//
//    if (jef_operations->mkdir != NULL) {
//        jef_operations->mkdir = jef_mkdir;
//    }
//
//    if (jef_operations->unlink != NULL) {
//        jef_operations->unlink = jef_unlink;
//    }
//
//    if (jef_operations->rmdir != NULL) {
//        jef_operations->rmdir = jef_rmdir;
//    }
//
//    if (jef_operations->symlink != NULL) {
//        jef_operations->symlink = jef_symlink;
//    }
//
//    if (jef_operations->rename != NULL) {
//        jef_operations->rename = jef_rename;
//    }
//
//    if (jef_operations->link != NULL) {
//        jef_operations->link = jef_link;
//    }
//
//    if (jef_operations->chmod != NULL) {
//        jef_operations->chmod = jef_chmod;
//    }
//
//    if (jef_operations->chown != NULL) {
//        jef_operations->chown = jef_chown;
//    }
//
//    if (jef_operations->truncate != NULL) {
//        jef_operations->truncate = jef_truncate;
//    }
//
//    if (jef_operations->open != NULL) {
//        jef_operations->open = jef_open;
//    }
//
//    if (jef_operations->read != NULL) {
//        jef_operations->read = jef_read;
//    }
//
//    if (jef_operations->write != NULL) {
//        jef_operations->write = jef_write;
//    }
//
//    if (jef_operations->statfs != NULL) {
//        jef_operations->statfs = jef_statfs;
//    }
//
//    if (jef_operations->flush != NULL) {
//        jef_operations->flush = jef_flush;
//    }
//
//    if (jef_operations->release != NULL) {
//        jef_operations->release = jef_release;
//    }
//
//    if (jef_operations->fsync != NULL) {
//        jef_operations->fsync = jef_fsync;
//    }
//
//    if (jef_operations->setxattr != NULL) {
//        jef_operations->setxattr = jef_setxattr;
//    }
//
//    if (jef_operations->getxattr != NULL) {
//        jef_operations->getxattr = jef_getxattr;
//    }
//
//    if (jef_operations->listxattr != NULL) {
//        jef_operations->listxattr = jef_listxattr;
//    }
//
//    if (jef_operations->removexattr != NULL) {
//        jef_operations->removexattr = jef_removexattr;
//    }
//
//    if (jef_operations->opendir != NULL) {
//        jef_operations->opendir = jef_opendir;
//    }
//
//    if (jef_operations->readdir != NULL) {
//        jef_operations->readdir = jef_readdir;
//    }
//
//    if (jef_operations->releasedir != NULL) {
//        jef_operations->releasedir = jef_releasedir;
//    }
//
//    if (jef_operations->fsyncdir != NULL) {
//        jef_operations->fsyncdir = jef_fsyncdir;
//    }
//
//    if (jef_operations->init != NULL) {
//        jef_operations->init = jef_init;
//    }
//
//    if (jef_operations->destroy != NULL) {
//        jef_operations->destroy = jef_destroy;
//    }
//
//    if (jef_operations->access != NULL) {
//        jef_operations->access = jef_access;
//    }
//
//    if (jef_operations->create != NULL) {
//        jef_operations->create = jef_create;
//    }
//
//    if (jef_operations->ftruncate != NULL) {
//        jef_operations->ftruncate = jef_ftruncate;
//    }
//
//    if (jef_operations->fgetattr != NULL) {
//        jef_operations->fgetattr = jef_fgetattr;
//    }
//
//    if (jef_operations->lock != NULL) {
//        jef_operations->lock = jef_lock;
//    }
//
//    if (jef_operations->utimens != NULL) {
//        jef_operations->utimens = jef_utimens;
//    }
//
//    if (jef_operations->bmap != NULL) {
//        jef_operations->bmap = jef_bmap;
//    }
//
//    if (jef_operations->ioctl != NULL) {
//        jef_operations->ioctl = jef_ioctl;
//    }
//
//    if (jef_operations->poll != NULL) {
//        jef_operations->poll = jef_poll;
//    }

    jint num = 2;
    return num;
    //need to pass the operations object from java, and accept int return type
    //convert operations object to struct
    //when converting the operations object to struct, need to maintain the methods from the class which they are
    //defined in, as this will allow invoking it when. may be able to store the pointer to env to call methods

//    return jef_register(argc, argv, &efs_operations, sizeof(efs_operations));
}
