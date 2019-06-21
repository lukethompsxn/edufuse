package filesystem;

import data.*;

/**
 * This class should be extended. Then override any methods which you wish to implement for your file system.
 * Only operations which are implemented by your file system should be added to the registered operations map.
 */
public abstract class AbstractFS {

    /** Get file attributes*/
    public int getattr(String path, Stat stat) {
        return 0;
    }

    /** Read the target of a symbolic link */
    public int readlink(String path, String buf, long len) {
        return 0;
    }

    /** Create a file node */
    public int mknod(String path, int mode, long rdev) {
        return 0;
    }

    /** Create a directory */
    public int mkdir(String path, int mode) {
        return 0;
    }

    /** Remove a file */
    public int unlink(String path) {
        return 0;
    }

    /** Remove a directory */
    public int rmdir(String path) {
        return 0;
    }

    /** Create a symbolic link */
    public int symlink(String linkname, String path) {
        return 0;
    }

    /** Rename a file */
    public int rename(String oldpath, String newpath) {
        return 0;
    }

    /** Create a hard link to a file */
    public int link(String oldpath, String newpath) {
        return 0;
    }

    /** Change the permission bits of a file */
    public int chmod(String path, int mode) {
        return 0;
    }

    /** Change the owner and group of a file */
    public int chown(String path, int uid, int gid) {
        return 0;
    }

    /** Change the size of a file */
    public int truncate(String path, long size) {
        return 0;
    }

    /** File open operation */
    public int open(String path, FUSEFileInfo fileInfo) {
        return 0;
    }

    /** Read data from an open file */
    public int read(String path, String buf, long size, long off, FUSEFileInfo fileInfo) {
        return 0;
    }

    /** Write data to an open file */
    public int write(String path, String buf, long size, long off, FUSEFileInfo fileInfo) {
        return 0;
    }

    /** Get file system statistics */
    public int statfs(String path, StatVFS statVFS) {
        return 0;
    }

    /** Possibly flush cached data */
    public int flush(String path, FUSEFileInfo fileInfo) {
        return 0;
    }

    /** Release an open file */
    public int release(String path, FUSEFileInfo fileInfo) {
        return 0;
    }

    /** Synchronize file contents */
    public int fsync(String path, int datasync, FUSEFileInfo fileInfo) {
        return 0;
    }

    /** Set extended attributes */
    public int setxattr(String path, String name, String value, long size, int flags) {
        return 0;
    }

    /** Get extended attributes */
    public int getxattr(String path, String name, String value, long size) {
        return 0;
    }

    /** List extended attributes */
    public int listxattr(String path, String list, long size) {
        return 0;
    }

    /** Remove extended attributes */
    public int removexattr(String path, String name) {
        return 0;
    }

    /** Open directory */
    public int opendir(String path, FUSEFileInfo fileInfo) {
        return 0;
    }

    /** Read directory */
    public int readdir(String path, FUSEFillDir filler, long off, FUSEFileInfo fileInfo) {
        return 0;
    }

    /** Release directory */
    public int releasedir(String path, FUSEFileInfo fileInfo) {
        return 0;
    }

    /** Synchronize directory contents */
    public int fsyncdir(String path, int datasync, FUSEFileInfo fileInfo) {
        return 0;
    }

    /** Clean up filesystem */
    public void destroy() { }

    /** Check file access permissions */
    public int access(String path, int mask) {
        return 0;
    }

    /** Create and open a file */
    public int create(String path, int mode, FUSEFileInfo fileInfo) {
        return 0;
    }

    /** Change the size of an open file */
    public int ftruncate(String path, long size, FUSEFileInfo fileInfo) {
        return 0;
    }

    /** Get attributes from an open file */
    public int fgetattr(String path, Stat stat, FUSEFileInfo fileInfo) {
        return 0;
    }

    /** Perform POSIX file locking operation */
    public int lock(String path, FUSEFileInfo fileInfo, int cmd, Flock lock) {
        return 0;
    }

    /** Change the access and modification times of a file with nanosecond resolution */
    public int utimens(String path, TimeSpec tv) {
        return 0;
    }

    /** Map block index within file to block index within device */
    public int bmap(String path, long blocksize, long idx) {
        return 0;
    }

    /** Ioctl */
    public int ioctl(String path, int cmd, FUSEFileInfo fileInfo, int flags) {
        return 0;
    }

//    /** Poll for IO readiness events */
//    public int poll(String path, FUSEFileInfo fileInfo, struct fuse_pollhandle *ph, long reventsp) {
//        return 0;
//    }
}
