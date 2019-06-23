package example;

import data.*;
import filesystem.AbstractFS;

//try to use reflection to work out what methods are overwritten to save the user from needing to manually specificy
public class ExampleFS extends AbstractFS {

    @Override
    public int getattr(String path, Stat stat) {
        System.out.println("java getattr got called!!!!");
        return super.getattr(path, stat);
    }

    @Override
    public int readlink(String path, String buf, long len) {
        System.out.println("java readlink got called!!!!");
        return super.readlink(path, buf, len);
    }

    @Override
    public int mknod(String path, int mode, long rdev) {
        System.out.println("java mknod got called!!!!");
        return super.mknod(path, mode, rdev);
    }

    @Override
    public int mkdir(String path, int mode) {
        System.out.println("java mkdir got called!!!!");
        return super.mkdir(path, mode);
    }

    @Override
    public int unlink(String path) {
        System.out.println("java unlink got called!!!!");
        return super.unlink(path);
    }

    @Override
    public int rmdir(String path) {
        System.out.println("java rmdir got called!!!!");
        return super.rmdir(path);
    }

    @Override
    public int symlink(String linkname, String path) {
        System.out.println("java symlink got called!!!!");
        return super.symlink(linkname, path);
    }

    @Override
    public int rename(String oldpath, String newpath) {
        System.out.println("java rename got called!!!!");
        return super.rename(oldpath, newpath);
    }

    @Override
    public int link(String oldpath, String newpath) {
        System.out.println("java link got called!!!!");
        return super.link(oldpath, newpath);
    }

    @Override
    public int chmod(String path, int mode) {
        System.out.println("java chmod got called!!!!");
        return super.chmod(path, mode);
    }

    @Override
    public int chown(String path, int uid, int gid) {
        System.out.println("java chown got called!!!!");
        return super.chown(path, uid, gid);
    }

    @Override
    public int truncate(String path, long size) {
        System.out.println("java truncate got called!!!!");
        return super.truncate(path, size);
    }

    @Override
    public int open(String path, FUSEFileInfo fileInfo) {
        System.out.println("java open got called!!!!");
        return super.open(path, fileInfo);
    }

    @Override
    public int read(String path, String buf, long size, long off, FUSEFileInfo fileInfo) {
        System.out.println("java read got called!!!!");
        return super.read(path, buf, size, off, fileInfo);
    }

    @Override
    public int write(String path, String buf, long size, long off, FUSEFileInfo fileInfo) {
        System.out.println("java write got called!!!!");
        return super.write(path, buf, size, off, fileInfo);
    }

    @Override
    public int statfs(String path, StatVFS statVFS) {
        System.out.println("java statfs got called!!!!");
        return super.statfs(path, statVFS);
    }

    @Override
    public int flush(String path, FUSEFileInfo fileInfo) {
        System.out.println("java flush got called!!!!");
        return super.flush(path, fileInfo);
    }

    @Override
    public int release(String path, FUSEFileInfo fileInfo) {
        System.out.println("java release got called!!!!");
        return super.release(path, fileInfo);
    }

    @Override
    public int fsync(String path, int datasync, FUSEFileInfo fileInfo) {
        System.out.println("java fsync got called!!!!");
        return super.fsync(path, datasync, fileInfo);
    }

    @Override
    public int setxattr(String path, String name, String value, long size, int flags) {
        System.out.println("java setxattr got called!!!!");
        return super.setxattr(path, name, value, size, flags);
    }

    @Override
    public int getxattr(String path, String name, String value, long size) {
        System.out.println("java getxattr got called!!!!");
        return super.getxattr(path, name, value, size);
    }

    @Override
    public int listxattr(String path, String list, long size) {
        System.out.println("java listxattr got called!!!!");
        return super.listxattr(path, list, size);
    }

    @Override
    public int removexattr(String path, String name) {
        System.out.println("java removexattr got called!!!!");
        return super.removexattr(path, name);
    }

    @Override
    public int opendir(String path, FUSEFileInfo fileInfo) {
        System.out.println("java opendir got called!!!!");
        return super.opendir(path, fileInfo);
    }

    @Override
    public int readdir(String path, long off, FUSEFileInfo fileInfo) {
        System.out.println("java readdir got called!!!!");
        return super.readdir(path, off, fileInfo);
    }

    @Override
    public int releasedir(String path, FUSEFileInfo fileInfo) {
        System.out.println("java releasedir got called!!!!");
        return super.releasedir(path, fileInfo);
    }

    @Override
    public int fsyncdir(String path, int datasync, FUSEFileInfo fileInfo) {
        System.out.println("java fsyncdir got called!!!!");
        return super.fsyncdir(path, datasync, fileInfo);
    }

    @Override
    public void destroy() {
        System.out.println("java destroy got called!!!!");
        super.destroy();
    }

    @Override
    public int access(String path, int mask) {
        System.out.println("java access got called!!!!");
        return super.access(path, mask);
    }

    @Override
    public int create(String path, int mode, FUSEFileInfo fileInfo) {
        System.out.println("java create got called!!!!");
        return super.create(path, mode, fileInfo);
    }

    @Override
    public int ftruncate(String path, long size, FUSEFileInfo fileInfo) {
        System.out.println("java ftruncate got called!!!!");
        return super.ftruncate(path, size, fileInfo);
    }

    @Override
    public int fgetattr(String path, Stat stat, FUSEFileInfo fileInfo) {
        System.out.println("java fgetattr got called!!!!");
        return super.fgetattr(path, stat, fileInfo);
    }

    @Override
    public int lock(String path, FUSEFileInfo fileInfo, int cmd, Flock lock) {
        System.out.println("java lock got called!!!!");
        return super.lock(path, fileInfo, cmd, lock);
    }

    @Override
    public int utimens(String path, TimeSpec tv) {
        System.out.println("java utimens got called!!!!");
        return super.utimens(path, tv);
    }

    @Override
    public int bmap(String path, long blocksize, long idx) {
        System.out.println("java bmap got called!!!!");
        return super.bmap(path, blocksize, idx);
    }

    @Override
    public int ioctl(String path, int cmd, FUSEFileInfo fileInfo, int flags) {
        System.out.println("java ioctl got called!!!!");
        return super.ioctl(path, cmd, fileInfo, flags);
    }
}
