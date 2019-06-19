//package util;
//
//import java.util.EnumMap;
//
///**
// * This class should be extended. Then override any methods which you wish to implement for your file system.
// * Only operations which are implemented by your file system should be added to the registered operations map.
// *
// * Add your method to the operations map using
// * operations.put(FUSEOperations.<operation>, (<parameters>) -> <method>(<parameters>));
// * For example
// * operations.put(FUSEOperations.getattr, (String path, String buf) -> getattr(path, buf));
// */
//public abstract class AbstractFS {
//    public EnumMap<FUSEOperations, Runnable> operations = new EnumMap<FUSEOperations, Runnable>(FUSEOperations.class);
//
//    /** Get file attributes*/
//    public int getattr(String path, struct stat *buf) {
//        return 0;
//    }
//
//    /** Read the target of a symbolic link */
//    public int readlink(String path, String buf, size_t len) {
//        return 0;
//    }
//
//    /** Create a file node */
//    public int mknod(String path, mode_t mode, dev_t rdev) {
//        return 0;
//    }
//
//    /** Create a directory */
//    public int mkdir(String path, mode_t mode) {
//        return 0;
//    }
//
//    /** Remove a file */
//    public int unlink(String path) {
//        return 0;
//    }
//
//    /** Remove a directory */
//    public int rmdir(String path) {
//        return 0;
//    }
//
//    /** Create a symbolic link */
//    public int symlink(String linkname, String path) {
//        return 0;
//    }
//
//    /** Rename a file */
//    public int rename(String oldpath, String newpath) {
//        return 0;
//    }
//
//    /** Create a hard link to a file */
//    public int link(String oldpath, String newpath) {
//        return 0;
//    }
//
//    /** Change the permission bits of a file */
//    public int chmod(String path, mode_t mode) {
//        return 0;
//    }
//
//    /** Change the owner and group of a file */
//    public int chown(String path, uid_t uid, gid_t gid) {
//        return 0;
//    }
//
//    /** Change the size of a file */
//    public int truncate(String path, off_t size) {
//        return 0;
//    }
//
//    /** File open operation */
//    public int open(String path, struct fuse_file_info *fi) {
//        return 0;
//    }
//
//    /** Read data from an open file */
//    public int read(String path, String buf, size_t size, off_t off, struct fuse_file_info *fi) {
//        return 0;
//    }
//
//    /** Write data to an open file */
//    public int write(String path, String buf, size_t size, off_t off, struct fuse_file_info *fi) {
//        return 0;
//    }
//
//    /** Get file system statistics */
//    public int statfs(String path, struct statvfs *buf) {
//        return 0;
//    }
//
//    /** Possibly flush cached data */
//    public int flush(String path, struct fuse_file_info *fi) {
//        return 0;
//    }
//
//    /** Release an open file */
//    public int release(String path, struct fuse_file_info *fi) {
//        return 0;
//    }
//
//    /** Synchronize file contents */
//    public int fsync(String path, int datasync, struct fuse_file_info *fi) {
//        return 0;
//    }
//
//    /** Set extended attributes */
//    public int setxattr(String path, String name, String value, size_t size, int flags) {
//        return 0;
//    }
//
//    /** Get extended attributes */
//    public int getxattr(String path, String name, String value, size_t size) {
//        return 0;
//    }
//
//    /** List extended attributes */
//    public int listxattr(String path, String list, size_t size) {
//        return 0;
//    }
//
//    /** Remove extended attributes */
//    public int removexattr(String path, String name) {
//        return 0;
//    }
//
//    /** Open directory */
//    public int opendir(String path, struct fuse_file_info *fi) {
//        return 0;
//    }
//
//    /** Read directory */
//    public int readdir(String path, void *buf, fuse_fill_dir_t filler, off_t off, struct fuse_file_info *fi) {
//        return 0;
//    }
//
//    /** Release directory */
//    public int releasedir(String path, struct fuse_file_info *fi) {
//        return 0;
//    }
//
//    /** Synchronize directory contents */
//    public int fsyncdir(String path, int datasync, struct fuse_file_info *fi) {
//        return 0;
//    }
//
//    /** Clean up filesystem */
//    public void destroy(void *data) {
//
//    }
//
//    /** Check file access permissions */
//    public int access(String path, int mask) {
//        return 0;
//    }
//
//    /** Create and open a file */
//    public int create(String path, mode_t mode, struct fuse_file_info *fi) {
//        return 0;
//    }
//
//    /** Change the size of an open file */
//    public int ftruncate(String path, off_t size, struct fuse_file_info *fi) {
//        return 0;
//    }
//
//    /** Get attributes from an open file */
//    public int fgetattr(String path, struct stat *buf, struct fuse_file_info *fi) {
//        return 0;
//    }
//
//    /** Perform POSIX file locking operation */
//    public int lock(String path, struct fuse_file_info *fi, int cmd, struct flock *lock) {
//        return 0;
//    }
//
//    /** Change the access and modification times of a file with nanosecond resolution */
//    public int utimens(String path, struct timespec tv[2]) {
//        return 0;
//    }
//
//    /** Map block index within file to block index within device */
//    public int bmap(String path, size_t blocksize, uint64_t *idx) {
//        return 0;
//    }
//
//    /** Ioctl */
//    public int ioctl(String path, int cmd, void *arg, struct fuse_file_info *fi, unsigned int flags, void *data) {
//        return 0;
//    }
//
//    /** Poll for IO readiness events */
//    public int poll(String path, struct fuse_file_info *fi, struct fuse_pollhandle *ph, unsigned *reventsp) {
//        return 0;
//    }
//}
