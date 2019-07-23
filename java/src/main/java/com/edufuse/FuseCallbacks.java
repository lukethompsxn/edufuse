package com.edufuse;

import jnr.ffi.Pointer;
import jnr.ffi.annotations.Delegate;
import jnr.ffi.types.dev_t;
import jnr.ffi.types.gid_t;
import jnr.ffi.types.mode_t;
import jnr.ffi.types.off_t;
import jnr.ffi.types.size_t;
import jnr.ffi.types.u_int32_t;
import jnr.ffi.types.uid_t;

/**
 * Retrieved from https://github.com/SerCeMan/jnr-fuse
 */
public final class FuseCallbacks {

    @FunctionalInterface
    public interface GetAttrCallback {
        @Delegate
        int getattr(String path, Pointer stbuf);
    }

    @FunctionalInterface
    public interface ReadlinkCallback {
        @Delegate
        int readlink(String path, Pointer buf, @size_t long size);
    }

    @FunctionalInterface
    public interface MknodCallback {
        @Delegate
        int mknod(String path, @mode_t long mode, @dev_t long rdev);
    }

    @FunctionalInterface
    public interface MkdirCallback {
        @Delegate
        int mkdir(String path, @mode_t long mode);
    }

    @FunctionalInterface
    public interface UnlinkCallback {
        @Delegate
        int unlink(String path);
    }

    @FunctionalInterface
    public interface RmdirCallback {
        @Delegate
        int rmdir(String path);
    }

    @FunctionalInterface
    public interface SymlinkCallback {
        @Delegate
        int symlink(String oldpath, String newpath);
    }

    @FunctionalInterface
    public interface RenameCallback {
        @Delegate
        int rename(String oldpath, String newpath);
    }

    @FunctionalInterface
    public interface LinkCallback {
        @Delegate
        int link(String oldpath, String newpath);
    }

    @FunctionalInterface
    public interface ChmodCallback {
        @Delegate
        int chmod(String path, @mode_t long mode);
    }

    @FunctionalInterface
    public interface ChownCallback {
        @Delegate
        int chown(String path, @uid_t long uid, @gid_t long gid);
    }

    @FunctionalInterface
    public interface TruncateCallback {
        @Delegate
        int truncate(String path, @off_t long size);
    }

    @FunctionalInterface
    public interface OpenCallback {
        @Delegate
        int open(String path, Pointer fi);
    }

    @FunctionalInterface
    public interface ReadCallback {
        @Delegate
        int read(String path, Pointer buf, @size_t long size, @off_t long offset, Pointer fi);
    }

    @FunctionalInterface
    public interface WriteCallback {
        @Delegate
        int write(String path, Pointer buf, @size_t long size, @off_t long offset, Pointer fi);
    }

    @FunctionalInterface
    public interface StatfsCallback {
        @Delegate
        int statfs(String path, Pointer stbuf);
    }

    @FunctionalInterface
    public interface FlushCallback {
        @Delegate
        int flush(String path, Pointer fi);
    }

    @FunctionalInterface
    public interface ReleaseCallback {
        @Delegate
        int release(String path, Pointer fi);
    }

    @FunctionalInterface
    public interface FsyncCallback {
        @Delegate
        int fsync(String path, int isdatasync, Pointer fi);
    }

    @FunctionalInterface
    public interface SetxattrCallback {
        @Delegate
        int setxattr(String path, String name, Pointer value, @size_t long size, int flags);
    }

    @FunctionalInterface
    public interface GetxattrCallback {
        @Delegate
        int getxattr(String path, String name, Pointer value, @size_t long size);
    }

    @FunctionalInterface
    public interface ListxattrCallback {
        @Delegate
        int listxattr(String path, Pointer list, @size_t long size);
    }

    @FunctionalInterface
    public interface RemovexattrCallback {
        @Delegate
        int removexattr(String path, String name);
    }

    @FunctionalInterface
    public interface OpendirCallback {
        @Delegate
        int opendir(String path, Pointer fi);
    }

    @FunctionalInterface
    public interface ReaddirCallback {
        @Delegate
        int readdir(String path, Pointer buf, Pointer filter, @off_t long offset, Pointer fi);
    }

    @FunctionalInterface
    public interface ReleasedirCallback {
        @Delegate
        int releasedir(String path, Pointer fi);
    }

    @FunctionalInterface
    public interface FsyncdirCallback {
        @Delegate
        int fsyncdir(String path, Pointer fi);
    }

    @FunctionalInterface
    public interface InitCallback {
        @Delegate
        public Pointer init(Pointer conn);
    }

    @FunctionalInterface
    public interface DestroyCallback {
        @Delegate
        void destroy(Pointer initResult);
    }

    @FunctionalInterface
    public interface AccessCallback {
        @Delegate
        int access(String path, int mask);
    }

    @FunctionalInterface
    public interface CreateCallback {
        @Delegate
        int create(String path, @mode_t long mode, Pointer fi);
    }

    @FunctionalInterface
    public interface FtruncateCallback {
        @Delegate
        int ftruncate(String path, @off_t long size, Pointer fi);
    }

    @FunctionalInterface
    public interface FgetattrCallback {
        @Delegate
        int fgetattr(String path, Pointer stbuf, Pointer fi);
    }

    @FunctionalInterface
    public interface LockCallback {
        @Delegate
        int lock(String path, Pointer fi, int cmd, Pointer flock);
    }

    @FunctionalInterface
    public interface UtimensCallback {
        @Delegate
        int utimens(String path, Pointer timespec);
    }

    @FunctionalInterface
    public interface BmapCallback {
        @Delegate
        int bmap(String path, @size_t long blocksize, Pointer idx);
    }

    @FunctionalInterface
    public interface IoctlCallback {
        @Delegate
        int ioctl(String path, int cmd, Pointer arg, Pointer fi, @u_int32_t long flags, Pointer data);
    }

    @FunctionalInterface
    public interface PollCallback {
        @Delegate
        int poll(String path, Pointer fi, Pointer ph, Pointer reventsp);
    }

    @FunctionalInterface
    public interface WritebufCallback {
        @Delegate
        int write_buf(String path, Pointer buf, @off_t long off, Pointer fi);
    }

    @FunctionalInterface
    public interface ReadbufCallback {
        @Delegate
        int read_buf(String path, Pointer bufp, @size_t long size, @off_t long off, Pointer fi);
    }

    @FunctionalInterface
    public interface FlockCallback {
        @Delegate
        int flock(String path, Pointer fi, int op);
    }

    @FunctionalInterface
    public interface FallocateCallback {
        @Delegate
        int fallocate(String path, int mode, @off_t long off, @off_t long length, Pointer fi);
    }

    private FuseCallbacks() {
    }
}
