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
JNIEnv *env;
jclass abstract_fs_c;
jobject filesystem;
jclass stat_c;
jmethodID stat_constructor_m;
jclass fuse_file_info_c;
jmethodID fuse_file_info_constructor_m;
jclass statvfs_c;
jmethodID statvfs_constructor_m;
jclass fuse_fill_dir_c;
jmethodID fuse_fill_dir_constructor_m;
jclass flock_c;
jmethodID flock_constructor_m;
jclass timespec_c;
jmethodID timespec_constructor_m;

/**
 *
 *
 * JNI Helper Methods
 *
 */

static void create_jni_objects()
{
    stat_c = (*env)->FindClass(env, "data/Stat");
    stat_constructor_m = (*env)->GetMethodID(env, stat_c, "<init>", "(JIJIJIIIJIJIJ)V");

    fuse_file_info_c = (*env)->FindClass(env, "data/FUSEFileInfo");
    fuse_file_info_constructor_m = (*env)->GetMethodID(env, fuse_file_info_c, "<init>", "(IILjava/lang/Boolean;Ljava/lang/Boolean;Ljava/lang/Boolean;Ljava/lang/Boolean;Ljava/lang/Boolean;Ljava/lang/Integer;JJ)V");

    statvfs_c = (*env)->FindClass(env, "data/StatVFS");
    statvfs_constructor_m = (*env)->GetMethodID(env, statvfs_c, "<init>", "(JJJJJJJJJJ)V");

    fuse_fill_dir_c = (*env)->FindClass(env, "data/FUSEFillDir");
    fuse_fill_dir_constructor_m = (*env)->GetMethodID(env, fuse_fill_dir_c, "<init>", "(Ljava/lang/String;Ldata/Stat;J)V");

    flock_c = (*env)->FindClass(env, "data/Flock");
    flock_constructor_m = (*env)->GetMethodID(env, flock_c, "<init>", "(IIJJI)V");

    timespec_c = (*env)->FindClass(env, "data/TimeSpec");
    timespec_constructor_m = (*env)->GetMethodID(env, timespec_c, "<init>", "(JJJJ)V");
}

static int operations_contains(jobject operations_map, jmethodID contains_m, char *str)
{
    char *data= (char*)malloc(sizeof(str));
    strcpy(data, str);
    jstring jstr = (*env)->NewStringUTF(env, data);
    int bool = (int)(*env)->CallObjectMethod(env, operations_map, contains_m, jstr);
    free(data);
    return bool;
}

static jstring convert_chars(const char *cstr)
{
    char *str = (char*)malloc(sizeof(cstr));
    strcpy(str, cstr);
    jstring jstr = (*env)->NewStringUTF(env, str);
    free(str);
    return jstr;
}

static jobject convert_stat(struct stat *stbuf)
{
    jobject stat = (*env)->NewObject(
            env,
            stat_c,
            stat_constructor_m,
            (jlong) stbuf->st_dev,
            (jint) 0, // __pad1
            (jlong) stbuf->st_ino,
            (jint) stbuf->st_mode,
            (jlong) stbuf->st_nlink,
            (jint) stbuf->st_uid,
            (jint) stbuf->st_gid,
            (jint) stbuf->__pad0,
            (jlong) stbuf->st_rdev,
            (jint) 0, // __pad2
            (jlong) stbuf->st_size,
            (jint) stbuf->st_blksize,
            (jlong) stbuf->st_blocks);

    return stat;
}

static jobject convert_fuse_file_info(struct fuse_file_info *fi)
{
    jobject fuse_file_info = (*env)->NewObject(
            env,
            fuse_file_info_c,
            fuse_file_info_constructor_m,
            (jint) fi->flags,
            (jint) fi->writepage,
            (jboolean) fi->direct_io,
            (jboolean) fi->keep_cache,
            (jboolean) fi->flush,
            (jboolean) fi->nonseekable,
            (jboolean) fi->flock_release,
            (jint) fi->padding,
            (jlong) fi->fh,
            (jlong) fi->lock_owner);

    return fuse_file_info;
}

static jobject convert_statvfs(struct statvfs *buf)
{
    jobject statvfs = (*env)->NewObject(
            env,
            statvfs_c,
            statvfs_constructor_m,
            (jlong) buf->f_bsize,
            (jlong) buf->f_frsize,
            (jlong) buf->f_blocks,
            (jlong) buf->f_bavail,
            (jlong) buf->f_files,
            (jlong) buf->f_ffree,
            (jlong) buf->f_favail,
            (jlong) buf->f_fsid,
            (jlong) buf->f_flag,
            (jlong) buf->f_namemax);

    return statvfs;
}

// fuse fill dir is a method, need to make a call back to C to invoke this
//static jobject convert_fuse_fill_dir(fuse_fill_dir_t *filler)
//{
//    jobject fuse_fill_dir = (*env)->NewObject(
//            env,
//            fuse_fill_dir_c,
//            fuse_fill_dir_constructor_m,
//            convert_chars() filler
//            )
//}

static jobject convert_flock(struct flock *lock)
{
    jobject flock = (*env)->NewObject(
            env,
            flock_c,
            flock_constructor_m,
            (jint) lock->l_type,
            (jint) lock->l_whence,
            (jlong) lock->l_start,
            (jlong) lock->l_len,
            (jint) lock->l_pid);

    return flock;
}

static jobject convert_timespec(struct timespec *tv[2])
{
    jobject timespec = (*env)->NewObject(
            env,
            timespec_c,
            timespec_constructor_m,
            (jlong) tv[0]->tv_sec,
            (jlong) tv[0]->tv_nsec,
            (jlong) tv[1]->tv_sec,
            (jlong) tv[1]->tv_nsec);

    return timespec;
}

/**
 *
 * eduFUSE Functions
 *
 */

/** Get file attributes */
static int jef_getattr(const char *path, struct stat *stbuf)
{
    jmethodID getattr_m = (*env)->GetMethodID(env, abstract_fs_c, "getattr", "(Ljava/lang/String;Ldata/Stat;)I");
    return (*env)->CallObjectMethod(env, filesystem, getattr_m, convert_chars(path), convert_stat(stbuf));
}

/** Read the target of a symbolic link */
static int jef_readlink(const char *path, char *buf, size_t len)
{
    jmethodID readlink_m = (*env)->GetMethodID(env, abstract_fs_c, "readlink", "(Ljava/lang/String;Ljava/lang/String;J)I");
    return (*env)->CallObjectMethod(env, filesystem, readlink_m, convert_chars(path), convert_chars(buf), (jlong) len);
    //todo test this function
}

/** Create a file node */
static int jef_mknod(const char *path, mode_t mode, dev_t rdev)
{
    jmethodID mknod_m = (*env)->GetMethodID(env, abstract_fs_c, "mknod", "(Ljava/lang/String;IJ)I");
    return (*env)->CallObjectMethod(env, filesystem, mknod_m, convert_chars(path), (jint) mode, (jlong) rdev);
    //todo test this function
}

/** Create a directory */
static int jef_mkdir(const char *path, mode_t mode)
{
    jmethodID mkdir_m = (*env)->GetMethodID(env, abstract_fs_c, "mkdir", "(Ljava/lang/String;I)I");
    return (*env)->CallObjectMethod(env, filesystem, mkdir_m, convert_chars(path), (jint) mode);
    //todo test this function
}

/** Remove a file */
static int jef_unlink(const char *path)
{
    jmethodID unlink_m = (*env)->GetMethodID(env, abstract_fs_c, "unlink", "(Ljava/lang/String;)I");
    return (*env)->CallObjectMethod(env, filesystem, unlink_m, convert_chars(path));
    //todo test this function
}

/** Remove a directory */
static int jef_rmdir(const char *path)
{
    jmethodID rmdir_m = (*env)->GetMethodID(env, abstract_fs_c, "rmdir", "(Ljava/lang/String;)I");
    return (*env)->CallObjectMethod(env, filesystem, rmdir_m, convert_chars(path));
    //todo test this function;
}

/** Create a symbolic link */
static int jef_symlink(const char *linkname, const char *path)
{
    jmethodID symlink_m = (*env)->GetMethodID(env, abstract_fs_c, "symlink", "(Ljava/lang/String;Ljava/lang/String;)I");
    return (*env)->CallObjectMethod(env, filesystem, symlink_m, convert_chars(linkname), convert_chars(path));
    //todo test this function
}

/** Rename a file */
static int jef_rename(const char *oldpath, const char *newpath)
{
    jmethodID rename_m = (*env)->GetMethodID(env, abstract_fs_c, "rename", "(Ljava/lang/String;Ljava/lang/String;)I");
    return (*env)->CallObjectMethod(env, filesystem, rename_m, convert_chars(oldpath), convert_chars(newpath));
    //todo test this function
}

/** Create a hard link to a file */
static int jef_link(const char *oldpath, const char *newpath)
{
    jmethodID link_m = (*env)->GetMethodID(env, abstract_fs_c, "link", "(Ljava/lang/String;Ljava/lang/String;)I");
    return (*env)->CallObjectMethod(env, filesystem, link_m, convert_chars(oldpath), convert_chars(newpath));
    //todo test this function
}

/** Change the permission bits of a file */
static int jef_chmod(const char *path, mode_t mode)
{
    jmethodID chmod_m = (*env)->GetMethodID(env, abstract_fs_c, "chmod", "(Ljava/lang/String;I)I");
    return (*env)->CallObjectMethod(env, filesystem, chmod_m, convert_chars(path), (jint) mode);
    //todo test this function
}

/** Change the owner and group of a file */
static int jef_chown(const char *path, uid_t uid, gid_t gid)
{
    jmethodID chown_m = (*env)->GetMethodID(env, abstract_fs_c, "chown", "(Ljava/lang/String;II)I");
    return (*env)->CallObjectMethod(env, filesystem, chown_m, convert_chars(path), (jint) uid, (jint) gid);
    //todo test this function
}

/** Change the size of a file */
static int jef_truncate(const char *path, off_t size)
{
    jmethodID truncate_m = (*env)->GetMethodID(env, abstract_fs_c, "truncate", "(Ljava/lang/String;J)I");
    return (*env)->CallObjectMethod(env, filesystem, truncate_m, convert_chars(path), (jlong) size);
    //todo test this function
}

/** File open operation */
static int jef_open(const char *path, struct fuse_file_info *fi)
        {
    jmethodID open_m = (*env)->GetMethodID(env, abstract_fs_c, "open", "(Ljava/lang/String;Ldata/FUSEFileInfo;)I");
    return (*env)->CallObjectMethod(env, filesystem, open_m, convert_chars(path), convert_fuse_file_info(fi));
    //todo test this function
}

/** Read data from an open file */
static int jef_read(const char *path, char *buf, size_t size, off_t off, struct fuse_file_info *fi) {
    jmethodID read_m = (*env)->GetMethodID(env, abstract_fs_c, "read", "(Ljava/lang/String;Ljava/lang/String;JJLdata/FUSEFileInfo;)I");
    return (*env)->CallObjectMethod(env, filesystem, read_m, convert_chars(path), convert_chars(buf), (jlong) size, (jlong) off, convert_fuse_file_info(fi));
    //todo test this function
}

/** Write data to an open file */
static int jef_write(const char *path, const char *buf, size_t size, off_t off, struct fuse_file_info *fi) {
    jmethodID write_m = (*env)->GetMethodID(env, abstract_fs_c, "write", "(Ljava/lang/String;Ljava/lang/String;JJLdata/FUSEFileInfo;)I");
    return (*env)->CallObjectMethod(env, filesystem, write_m, convert_chars(path), convert_chars(buf), (jlong) size, (jlong) off, convert_fuse_file_info(fi));
    //todo test this function
}

/** Get file system statistics */
static int jef_statfs(const char *path, struct statvfs *buf) {
    jmethodID statfs_m = (*env)->GetMethodID(env, abstract_fs_c, "statfs", "(Ljava/lang/String;Ldata/StatVFS;)I");
    return (*env)->CallObjectMethod(env, filesystem, statfs_m, convert_chars(path), convert_statvfs(buf));
    //todo test this function
}

/** Possibly flush cached data */
static int jef_flush(const char *path, struct fuse_file_info *fi) {
    jmethodID flush_m = (*env)->GetMethodID(env, abstract_fs_c, "flush", "(Ljava/lang/String;Ldata/FUSEFileInfo;)I");
    return (*env)->CallObjectMethod(env, filesystem, flush_m, convert_chars(path), convert_fuse_file_info(fi));
    //todo test this function
}

/** Release an open file */
static int jef_release(const char *path, struct fuse_file_info *fi) {
    jmethodID release_m = (*env)->GetMethodID(env, abstract_fs_c, "release", "(Ljava/lang/String;Ldata/FUSEFileInfo;)I");
    return (*env)->CallObjectMethod(env, filesystem, release_m, convert_chars(path), convert_fuse_file_info(fi));
    //todo test this function
}

/** Synchronize file contents */
static int jef_fsync(const char *path, int datasync, struct fuse_file_info *fi) {
    jmethodID fsync_m = (*env)->GetMethodID(env, abstract_fs_c, "fsync", "(Ljava/lang/String;ILdata/FUSEFileInfo;)I");
    return (*env)->CallObjectMethod(env, filesystem, fsync_m, convert_chars(path), (jint) datasync, convert_fuse_file_info(fi));
    //todo test this function
}

/** Set extended attributes */
static int jef_setxattr(const char *path, const char *name, const char *value, size_t size, int flags) {
    jmethodID setxattr_m = (*env)->GetMethodID(env, abstract_fs_c, "setxattr", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;JI)I");
    return (*env)->CallObjectMethod(env, filesystem, setxattr_m, convert_chars(path), convert_chars(name), convert_chars(value), (jlong) size, (jint) flags);
    //todo test this function
}

/** Get extended attributes */
static int jef_getxattr(const char *path, const char *name, char *value, size_t size) {
    jmethodID getxattr_m = (*env)->GetMethodID(env, abstract_fs_c, "getxattr", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;J)I");
    return (*env)->CallObjectMethod(env, filesystem, getxattr_m, convert_chars(path), convert_chars(name), convert_chars(value), (jlong) size);
    //todo test this function
}

/** List extended attributes */
static int jef_listxattr(const char *path, char *list, size_t size) {
    jmethodID listxattr_m = (*env)->GetMethodID(env, abstract_fs_c, "listxattr", "(Ljava/lang/String;Ljava/lang/String;J)I");
    return (*env)->CallObjectMethod(env, filesystem, list, convert_chars(path), convert_chars(list), (jlong) size);
    //todo test this function
}

/** Remove extended attributes */
static int jef_removexattr(const char *path, const char *name) {
    jmethodID removexattr_m = (*env)->GetMethodID(env, abstract_fs_c, "removexattr", "(Ljava/lang/String;Ljava/lang/String;)I");
    return (*env)->CallObjectMethod(env, filesystem, removexattr_m, convert_chars(path), convert_chars(name));
    //todo test this function
}

/** Open directory */
static int jef_opendir(const char *path, struct fuse_file_info *fi) {
    jmethodID opendir_m = (*env)->GetMethodID(env, abstract_fs_c, "opendir", "(Ljava/lang/String;Ldata/FUSEFileInfo;)I");
    return (*env)->CallObjectMethod(env, filesystem, opendir_m, convert_chars(path), convert_fuse_file_info(fi));
    //todo test this function
}

/** Read directory */
static int jef_readdir(const char *path, void *buf, fuse_fill_dir_t filler, off_t off, struct fuse_file_info *fi) {
    jmethodID readdir_m = (*env)->GetMethodID(env, abstract_fs_c, "readdir", "(Ljava/lang/String;Ldata/FUSEFillDir;JLdata/FUSEFileInfo;)I");
    //trialling just not passing the method
//    return (*env)->CallObjectMethod(env, filesystem, readdir_m, convert_chars(path), convert_fuse_fill_dir(&filler), (jlong) off, convert_fuse_file_info(fi));
    return 0;
    //todo finish
}

/** Release directory */
static int jef_releasedir(const char *path, struct fuse_file_info *fi) {
    jmethodID releasedir_m = (*env)->GetMethodID(env, abstract_fs_c, "releasedir", "(Ljava/lang/String;Ldata/FUSEFileInfo;)I");
    return (*env)->CallObjectMethod(env, filesystem, releasedir_m, convert_chars(path), convert_fuse_file_info(fi));
    //todo test this function
}

/** Synchronize directory contents */
static int jef_fsyncdir(const char *path, int datasync, struct fuse_file_info *fi) {
    jmethodID fsyncdir_m = (*env)->GetMethodID(env, abstract_fs_c, "fsyncdir", "(Ljava/lang/String;ILdata/FUSEFileInfo;)I");
    return (*env)->CallObjectMethod(env, filesystem, fsyncdir_m, convert_chars(path), (jint) datasync, convert_fuse_file_info(fi));
    //todo test this function
}

/** Initialize filesystem */
static void *jef_init(struct fuse_conn_info *conn) {
//    jmethodID init_m = (*env)->GetMethodID(env, abstract_fs_c, "init", "");
    //todo finish this
    return 0;
}

/**
 * Clean up filesystem */
static void jef_destroy(void *data) {
    jmethodID destroy_m = (*env)->GetMethodID(env, abstract_fs_c, "destroy", "()V");
    (*env)->CallObjectMethod(env, filesystem, destroy_m);
    //todo test this function
}

/** Check file access permissions */
static int jef_access(const char *path, int mask) {
    jmethodID access_m = (*env)->GetMethodID(env, abstract_fs_c, "access", "(Ljava/lang/String;I)I");
    return (*env)->CallObjectMethod(env, filesystem, access_m, convert_chars(path), (jint) mask);
    //todo test this function
}

/**
 * Create and open a file */
static int jef_create(const char *path, mode_t mode, struct fuse_file_info *fi) {
    jmethodID create_m = (*env)->GetMethodID(env, abstract_fs_c, "create", "(Ljava/lang/String;ILdata/FUSEFileInfo;)I");
    return (*env)->CallObjectMethod(env, filesystem, create_m, convert_chars(path), (jint) mode, convert_fuse_file_info(fi));
    //todo test this function
}

/** Change the size of an open file */
static int jef_ftruncate(const char *path, off_t size, struct fuse_file_info *fi) {
    jmethodID ftruncate_m = (*env)->GetMethodID(env, abstract_fs_c, "ftruncate", "(Ljava/lang/String;JLdata/FUSEFileInfo;)I");
    return (*env)->CallObjectMethod(env, filesystem, ftruncate_m, convert_chars(path), (jlong) size, convert_fuse_file_info(fi));
    //todo test this function
}

/** Get attributes from an open file */
static int jef_fgetattr(const char *path, struct stat *buf, struct fuse_file_info *fi) {
    jmethodID fgetattr_m = (*env)->GetMethodID(env, abstract_fs_c, "fgetattr", "(Ljava/lang/String;Ldata/Stat;Ldata/FUSEFileInfo;)I");
    return (*env)->CallObjectMethod(env, filesystem, fgetattr_m, convert_chars(path), convert_stat(buf), convert_fuse_file_info(fi));
    //todo test this function
}

/** Perform POSIX file locking operation */
static int jef_lock(const char *path, struct fuse_file_info *fi, int cmd, struct flock *lock) {
    jmethodID lock_m = (*env)->GetMethodID(env, abstract_fs_c, "lock", "(Ljava/lang/String;Ldata/FUSEFileInfo;ILdata/Flock;)I");
    return (*env)->CallObjectMethod(env, filesystem, lock_m, convert_chars(path), convert_fuse_file_info(fi), (jint) cmd, convert_flock(lock));
    //todo test this function
}

/** Change the access and modification times of a file with */
static int jef_utimens(const char *path, const struct timespec tv[2]) {
    jmethodID utimens_m = (*env)->GetMethodID(env, abstract_fs_c, "utimens", "(Ljava/lang/String;Ldata/TimeSpec;)I");
    return (*env)->CallObjectMethod(env, filesystem, utimens_m, convert_chars(path), convert_timespec(&tv));
    //todo test this function
}

/** Map block index within file to block index within device */
static int jef_bmap(const char *path, size_t blocksize, uint64_t *idx) {
    jmethodID bmap_m = (*env)->GetMethodID(env, abstract_fs_c, "bmap", "Ljava/lang/String;JJ)I");
    return (*env)->CallObjectMethod(env, filesystem, bmap_m, convert_chars(path), (jlong) blocksize, (jlong) idx); //the cast of this last param may throw seggy?
    //todo test this function
}

/** Ioctl */
static int jef_ioctl(const char *path, int cmd, void *arg, struct fuse_file_info *fi, unsigned int flags, void *data) {
    jmethodID ioctl_m = (*env)->GetMethodID(env, abstract_fs_c, "ioctl", "(Ljava/lang/String;ILdata/FUSEFileInfo;I)I");
    // trialling just not passing two methods.
    return (*env)->CallObjectMethod(env, filesystem, ioctl_m, convert_chars(path), (jint) cmd, convert_fuse_file_info(fi), (jint) flags);
    //todo test this function
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

JNIEXPORT jint JNICALL Java_util_FUSELink_registerOperations(JNIEnv *jniEnv, jobject this, jobject operations_map, jobjectArray args)
{
    env = jniEnv;
    abstract_fs_c = (*env)->FindClass(env, "filesystem/AbstractFS");

    jclass function_map_c = (*env)->FindClass(env, "util/FunctionMap");
    jmethodID contains_m = (*env)->GetMethodID(env, function_map_c, "contains", "(Ljava/lang/String;)Z");

    jmethodID get_filesystem_m = (*env)->GetMethodID(env, function_map_c, "getFilesystem", "()Lfilesystem/AbstractFS;");
    filesystem = (*env)->CallObjectMethod(env, operations_map, get_filesystem_m);

    create_jni_objects();

    if (operations_contains(operations_map, contains_m, "getattr")) {
        jef_operations.getattr = jef_getattr;
    }

    if (operations_contains(operations_map, contains_m, "readlink")) {
        jef_operations.readlink = jef_readlink;
    }

    if (operations_contains(operations_map, contains_m, "mknod")) {
        jef_operations.mknod = jef_mknod;
    }

    if (operations_contains(operations_map, contains_m, "mkdir")) {
        jef_operations.mkdir = jef_mkdir;
    }

    if (operations_contains(operations_map, contains_m, "unlink")) {
        jef_operations.unlink = jef_unlink;
    }

    if (operations_contains(operations_map, contains_m, "rmdir")) {
        jef_operations.rmdir = jef_rmdir;
    }

    if (operations_contains(operations_map, contains_m, "symlink")) {
        jef_operations.symlink = jef_symlink;
    }

    if (operations_contains(operations_map, contains_m, "rename")) {
        jef_operations.rename = jef_rename;
    }

    if (operations_contains(operations_map, contains_m, "link")) {
        jef_operations.link = jef_link;
    }

    if (operations_contains(operations_map, contains_m, "chmod")) {
        jef_operations.chmod = jef_chmod;
    }

    if (operations_contains(operations_map, contains_m, "chown")) {
        jef_operations.chown = jef_chown;
    }

    if (operations_contains(operations_map, contains_m, "truncate")) {
        jef_operations.truncate = jef_truncate;
    }

    if (operations_contains(operations_map, contains_m, "open")) {
        jef_operations.open = jef_open;
    }

    if (operations_contains(operations_map, contains_m, "read")) {
        jef_operations.read = jef_read;
    }

    if (operations_contains(operations_map, contains_m, "write")) {
        jef_operations.write = jef_write;
    }

    if (operations_contains(operations_map, contains_m, "statfs")) {
        jef_operations.statfs = jef_statfs;
    }

    if (operations_contains(operations_map, contains_m, "flush")) {
        jef_operations.flush = jef_flush;
    }

    if (operations_contains(operations_map, contains_m, "release")) {
        jef_operations.release = jef_release;
    }

    if (operations_contains(operations_map, contains_m, "fsync")) {
        jef_operations.fsync = jef_fsync;
    }

    if (operations_contains(operations_map, contains_m, "setxattr")) {
        jef_operations.setxattr = jef_setxattr;
    }

    if (operations_contains(operations_map, contains_m, "getxattr")) {
        jef_operations.getxattr = jef_getxattr;
    }

    if (operations_contains(operations_map, contains_m, "listxattr")) {
        jef_operations.listxattr = jef_listxattr;
    }

    if (operations_contains(operations_map, contains_m, "removexattr")) {
        jef_operations.removexattr = jef_removexattr;
    }

    if (operations_contains(operations_map, contains_m, "opendir")) {
        jef_operations.opendir = jef_opendir;
    }

    if (operations_contains(operations_map, contains_m, "readdir")) {
        jef_operations.readdir = jef_readdir;
    }

    if (operations_contains(operations_map, contains_m, "releasedir")) {
        jef_operations.releasedir = jef_releasedir;
    }

    if (operations_contains(operations_map, contains_m, "fsyncdir")) {
        jef_operations.fsyncdir = jef_fsyncdir;
    }

    if (operations_contains(operations_map, contains_m, "init")) {
        jef_operations.init = jef_init;
    }

    if (operations_contains(operations_map, contains_m, "destroy")) {
        jef_operations.destroy = jef_destroy;
    }

    if (operations_contains(operations_map, contains_m, "access")) {
        jef_operations.access = jef_access;
    }

    if (operations_contains(operations_map, contains_m, "create")) {
        jef_operations.create = jef_create;
    }

    if (operations_contains(operations_map, contains_m, "ftruncate")) {
        jef_operations.ftruncate = jef_ftruncate;
    }

    if (operations_contains(operations_map, contains_m, "fgetattr")) {
        jef_operations.fgetattr = jef_fgetattr;
    }

    if (operations_contains(operations_map, contains_m, "lock")) {
        jef_operations.lock = jef_lock;
    }

    if (operations_contains(operations_map, contains_m, "utimens")) {
        jef_operations.utimens = jef_utimens;
    }

    if (operations_contains(operations_map, contains_m, "bmap")) {
        jef_operations.bmap = jef_bmap;
    }

    if (operations_contains(operations_map, contains_m, "ioctl")) {
        jef_operations.ioctl = jef_ioctl;
    }

    /* not currently supported
    if (operations_contains(operations_map, contains_m, "poll")) {
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
