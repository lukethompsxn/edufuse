#define FUSE_USE_VERSION 31

#include <jni.h>
#include <stdio.h>
#include "util_FUSELink.h"
#include "../lib/edufuse.h"
#include <fuse.h>
#include <string.h>
#include <errno.h>
#include <stdlib.h>

struct fuse_operations jef_operations;
static struct jobject *operations;

/**
 *
 * eduFUSE Functions
 *
 */

/** Get file attributes */
static int jef_getattr(const char *path, struct stat *stbuf) {
    return 0;
}

/** Read the target of a symbolic link */
static int jef_readlink(const char *path, char *buf, size_t len) {
    return 0;
}

/** Create a file node */
static int jef_mknod(const char *path, mode_t mode, dev_t rdev) {
    return 0;
}

/** Create a directory */
static int jef_mkdir(const char *path, mode_t mode) {
    return 0;
}

/** Remove a file */
static int jef_unlink(const char *path) {
    return 0;
}

/** Remove a directory */
static int jef_rmdir(const char *path) {
    return 0;
}

/** Create a symbolic link */
static int jef_symlink(const char *linkname, const char *path) {
    return 0;
}

/** Rename a file */
static int jef_rename(const char *oldpath, const char *newpath) {
    return 0;
}

/** Create a hard link to a file */
static int jef_link(const char *oldpath, const char *newpath) {
    return 0;
}

/** Change the permission bits of a file */
static int jef_chmod(const char *path, mode_t mode) {
    return 0;
}

/** Change the owner and group of a file */
static int jef_chown(const char *path, uid_t uid, gid_t gid) {
    return 0;
}

/** Change the size of a file */
static int jef_truncate(const char *path, off_t size) {
    return 0;
}

/** File open operation */
static int jef_open(const char *path, struct fuse_file_info *fi) {
    return 0;
}

/** Read data from an open file */
static int jef_read(const char *path, char *buf, size_t size, off_t off, struct fuse_file_info *fi) {
    return 0;
}

/** Write data to an open file */
static int jef_write(const char *path, const char *buf, size_t size, off_t off, struct fuse_file_info *fi) {
    return 0;
}

/** Get file system statistics */
static int jef_statfs(const char *path, struct statvfs *buf) {
    return 0;
}

/** Possibly flush cached data */
static int jef_flush(const char *path, struct fuse_file_info *fi) {
    return 0;
}

/** Release an open file */
static int jef_release(const char *path, struct fuse_file_info *fi) {
    return 0;
}

/** Synchronize file contents */
static int jef_fsync(const char *path, int datasync, struct fuse_file_info *fi) {
    return 0;
}

/** Set extended attributes */
static int jef_setxattr(const char *path, const char *name, const char *value, size_t size, int flags) {
    return 0;
}

/** Get extended attributes */
static int jef_getxattr(const char *path, const char *name, char *value, size_t size) {
    return 0;
}

/** List extended attributes */
static int jef_listxattr(const char *path, char *list, size_t size) {
    return 0;
}

/** Remove extended attributes */
static int jef_removexattr(const char *path, const char *name) {
    return 0;
}

/** Open directory */
static int jef_opendir(const char *path, struct fuse_file_info *fi) {
    return 0;
}

/** Read directory */
static int jef_readdir(const char *path, void *buf, fuse_fill_dir_t filler, off_t off, struct fuse_file_info *fi) {
    return 0;
}

/** Release directory */
static int jef_releasedir(const char *path, struct fuse_file_info *fi) {
    return 0;
}

/** Synchronize directory contents */
static int jef_fsyncdir(const char *path, int datasync, struct fuse_file_info *fi) {
    return 0;
}

/** Initialize filesystem */
static void *jef_init(struct fuse_conn_info *conn) {
    return 0;
}

/**
 * Clean up filesystem */
static void jef_destroy(void *data) {
}

/** Check file access permissions */
static int jef_access(const char *path, int mask) {
    return 0;
}

/**
 * Create and open a file */
static int jef_create(const char *path, mode_t mode, struct fuse_file_info *fi) {
    return 0;
}

/** Change the size of an open file */
static int jef_ftruncate(const char *path, off_t size, struct fuse_file_info *fi) {
    return 0;
}

/** Get attributes from an open file */
static int jef_fgetattr(const char *path, struct stat *buf, struct fuse_file_info *fi) {
    return 0;
}

/** Perform POSIX file locking operation */
static int jef_lock(const char *path, struct fuse_file_info *fi, int cmd, struct flock *lock) {
    return 0;
}

/** Change the access and modification times of a file with */
static int jef_utimens(const char *path, const struct timespec tv[2]) {
    return 0;
}

/** Map block index within file to block index within device */
static int jef_bmap(const char *path, size_t blocksize, uint64_t *idx) {
    return 0;
}

/** Ioctl */
static int jef_ioctl(const char *path, int cmd, void *arg, struct fuse_file_info *fi, unsigned int flags, void *data) {
    return 0;
}

///** Poll for IO readiness events */
//static int jef_poll(const char *path, struct fuse_file_info *fi, struct fuse_pollhandle *ph, unsigned *reventsp) {
//    return 0;
//}


/**
 *
 * Java Native Functions
 *
 */

 static int operations_contains(JNIEnv *env, jobject operations_map, jmethodID contains_m, char *str)
 {
     char *data= (char*)malloc(16);
     strcpy(data, str);
     jstring jstr = (*env)->NewStringUTF(env, data);
     int bool = (int)(*env)->CallObjectMethod(env, operations_map, contains_m, jstr);
     free(data);
     return bool;
 }

JNIEXPORT jint JNICALL Java_util_FUSELink_registerOperations(JNIEnv *env, jobject this, jobject operations_map, jobjectArray args)
{
    operations = malloc(sizeof(operations_map));
    memcpy(operations, &operations_map, sizeof(operations_map));

    jclass function_map_c = (*env)->FindClass(env, "util/FunctionMap");
    jmethodID contains_m = (*env)->GetMethodID(env, function_map_c, "contains", "(Ljava/lang/String;)Z");

    if (operations_contains(env, operations_map, contains_m, "getattr")) {
        jef_operations.getattr = jef_getattr;
    }

    if (operations_contains(env, operations_map, contains_m, "readlink")) {
        jef_operations.readlink = jef_readlink;
    }

    if (operations_contains(env, operations_map, contains_m, "mknod")) {
        jef_operations.mknod = jef_mknod;
    }

    if (operations_contains(env, operations_map, contains_m, "mkdir")) {
        jef_operations.mkdir = jef_mkdir;
    }

    if (operations_contains(env, operations_map, contains_m, "unlink")) {
        jef_operations.unlink = jef_unlink;
    }

    if (operations_contains(env, operations_map, contains_m, "rmdir")) {
        jef_operations.rmdir = jef_rmdir;
    }

    if (operations_contains(env, operations_map, contains_m, "symlink")) {
        jef_operations.symlink = jef_symlink;
    }

    if (operations_contains(env, operations_map, contains_m, "rename")) {
        jef_operations.rename = jef_rename;
    }

    if (operations_contains(env, operations_map, contains_m, "link")) {
        jef_operations.link = jef_link;
    }

    if (operations_contains(env, operations_map, contains_m, "chmod")) {
        jef_operations.chmod = jef_chmod;
    }

    if (operations_contains(env, operations_map, contains_m, "chown")) {
        jef_operations.chown = jef_chown;
    }

    if (operations_contains(env, operations_map, contains_m, "truncate")) {
        jef_operations.truncate = jef_truncate;
    }

    if (operations_contains(env, operations_map, contains_m, "open")) {
        jef_operations.open = jef_open;
    }

    if (operations_contains(env, operations_map, contains_m, "read")) {
        jef_operations.read = jef_read;
    }

    if (operations_contains(env, operations_map, contains_m, "write")) {
        jef_operations.write = jef_write;
    }

    if (operations_contains(env, operations_map, contains_m, "statfs")) {
        jef_operations.statfs = jef_statfs;
    }

    if (operations_contains(env, operations_map, contains_m, "flush")) {
        jef_operations.flush = jef_flush;
    }

    if (operations_contains(env, operations_map, contains_m, "release")) {
        jef_operations.release = jef_release;
    }

    if (operations_contains(env, operations_map, contains_m, "fsync")) {
        jef_operations.fsync = jef_fsync;
    }

    if (operations_contains(env, operations_map, contains_m, "setxattr")) {
        jef_operations.setxattr = jef_setxattr;
    }

    if (operations_contains(env, operations_map, contains_m, "getxattr")) {
        jef_operations.getxattr = jef_getxattr;
    }

    if (operations_contains(env, operations_map, contains_m, "listxattr")) {
        jef_operations.listxattr = jef_listxattr;
    }

    if (operations_contains(env, operations_map, contains_m, "removexattr")) {
        jef_operations.removexattr = jef_removexattr;
    }

    if (operations_contains(env, operations_map, contains_m, "opendir")) {
        jef_operations.opendir = jef_opendir;
    }

    if (operations_contains(env, operations_map, contains_m, "readdir")) {
        jef_operations.readdir = jef_readdir;
    }

    if (operations_contains(env, operations_map, contains_m, "releasedir")) {
        jef_operations.releasedir = jef_releasedir;
    }

    if (operations_contains(env, operations_map, contains_m, "fsyncdir")) {
        jef_operations.fsyncdir = jef_fsyncdir;
    }

    if (operations_contains(env, operations_map, contains_m, "init")) {
        jef_operations.init = jef_init;
    }

    if (operations_contains(env, operations_map, contains_m, "destroy")) {
        jef_operations.destroy = jef_destroy;
    }

    if (operations_contains(env, operations_map, contains_m, "access")) {
        jef_operations.access = jef_access;
    }

    if (operations_contains(env, operations_map, contains_m, "create")) {
        jef_operations.create = jef_create;
    }

    if (operations_contains(env, operations_map, contains_m, "ftruncate")) {
        jef_operations.ftruncate = jef_ftruncate;
    }

    if (operations_contains(env, operations_map, contains_m, "fgetattr")) {
        jef_operations.fgetattr = jef_fgetattr;
    }

    if (operations_contains(env, operations_map, contains_m, "lock")) {
        jef_operations.lock = jef_lock;
    }

    if (operations_contains(env, operations_map, contains_m, "utimens")) {
        jef_operations.utimens = jef_utimens;
    }

    if (operations_contains(env, operations_map, contains_m, "bmap")) {
        jef_operations.bmap = jef_bmap;
    }

    if (operations_contains(env, operations_map, contains_m, "ioctl")) {
        jef_operations.ioctl = jef_ioctl;
    }

    /* not currently supported
    if (operations_contains(env, operations_map, contains_m, "poll")) {
        jef_operations.poll = jef_poll;
    }
     */

    int argc = (*env)->GetArrayLength(env, args);
    char *argv[argc];

    for (int i = 0; i < argc; ++i)
    {
        jstring jarg = (*env)->GetObjectArrayElement(env, args, i);
        const char* arg = (*env)->GetStringUTFChars(env, jarg, 0);
        argv[i] = arg;
//        (*env)->ReleaseStringUTFChars(env, jarg, arg);       releasing these screws the values in the char array, but
//        (*env)->DeleteLocalRef(env, jarg);                    if we dont release them we may get memory leaks??
    }

    return edufuse_register(argc, argv, &jef_operations, sizeof(jef_operations));
}
