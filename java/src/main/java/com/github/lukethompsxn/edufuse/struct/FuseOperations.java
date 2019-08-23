package com.github.lukethompsxn.edufuse.struct;

import com.github.lukethompsxn.edufuse.util.FuseCallbacks;
import jnr.ffi.BaseStruct;
import jnr.ffi.NativeType;
import jnr.ffi.Runtime;
import jnr.posix.util.Platform;

/**
 * fuse_operations struct
 *
 * @author Sergey Tselovalnikov
 * @since 30.05.15
 * Retrieved from https://github.com/SerCeMan/jnr-fuse
 */
public class FuseOperations extends BaseStruct {
    public FuseOperations(Runtime runtime) {
        super(runtime);
    }

    public final Func<FuseCallbacks.GetAttrCallback> getattr = func(FuseCallbacks.GetAttrCallback.class);
    public final Func<FuseCallbacks.ReadlinkCallback> readlink = func(FuseCallbacks.ReadlinkCallback.class);
    @Deprecated
    public final Func<FuseCallbacks.GetAttrCallback> getdir = func(FuseCallbacks.GetAttrCallback.class);
    public final Func<FuseCallbacks.MknodCallback> mknod = func(FuseCallbacks.MknodCallback.class);
    public final Func<FuseCallbacks.MkdirCallback> mkdir = func(FuseCallbacks.MkdirCallback.class);
    public final Func<FuseCallbacks.UnlinkCallback> unlink = func(FuseCallbacks.UnlinkCallback.class);
    public final Func<FuseCallbacks.RmdirCallback> rmdir = func(FuseCallbacks.RmdirCallback.class);
    public final Func<FuseCallbacks.SymlinkCallback> symlink = func(FuseCallbacks.SymlinkCallback.class);

    public final Func<FuseCallbacks.RenameCallback> rename = func(FuseCallbacks.RenameCallback.class);
    public final Func<FuseCallbacks.LinkCallback> link = func(FuseCallbacks.LinkCallback.class);
    public final Func<FuseCallbacks.ChmodCallback> chmod = func(FuseCallbacks.ChmodCallback.class);
    public final Func<FuseCallbacks.ChownCallback> chown = func(FuseCallbacks.ChownCallback.class);
    public final Func<FuseCallbacks.TruncateCallback> truncate = func(FuseCallbacks.TruncateCallback.class);
    @Deprecated
    public final Func<FuseCallbacks.GetAttrCallback> utime = func(FuseCallbacks.GetAttrCallback.class);
    public final Func<FuseCallbacks.OpenCallback> open = func(FuseCallbacks.OpenCallback.class);
    public final Func<FuseCallbacks.ReadCallback> read = func(FuseCallbacks.ReadCallback.class);
    public final Func<FuseCallbacks.WriteCallback> write = func(FuseCallbacks.WriteCallback.class);
    public final Func<FuseCallbacks.StatfsCallback> statfs = func(FuseCallbacks.StatfsCallback.class);
    public final Func<FuseCallbacks.FlushCallback> flush = func(FuseCallbacks.FlushCallback.class);
    public final Func<FuseCallbacks.ReleaseCallback> release = func(FuseCallbacks.ReleaseCallback.class);
    public final Func<FuseCallbacks.FsyncCallback> fsync = func(FuseCallbacks.FsyncCallback.class);
    public final Func<FuseCallbacks.SetxattrCallback> setxattr = func(FuseCallbacks.SetxattrCallback.class);
    public final Func<FuseCallbacks.GetxattrCallback> getxattr = func(FuseCallbacks.GetxattrCallback.class);
    public final Func<FuseCallbacks.ListxattrCallback> listxattr = func(FuseCallbacks.ListxattrCallback.class);
    public final Func<FuseCallbacks.RemovexattrCallback> removexattr = func(FuseCallbacks.RemovexattrCallback.class);
    public final Func<FuseCallbacks.OpendirCallback> opendir = func(FuseCallbacks.OpendirCallback.class);
    public final Func<FuseCallbacks.ReaddirCallback> readdir = func(FuseCallbacks.ReaddirCallback.class);
    public final Func<FuseCallbacks.ReleasedirCallback> releasedir = func(FuseCallbacks.ReleasedirCallback.class);
    public final Func<FuseCallbacks.FsyncdirCallback> fsyncdir = func(FuseCallbacks.FsyncdirCallback.class);
    public final Func<FuseCallbacks.InitCallback> init = func(FuseCallbacks.InitCallback.class);
    public final Func<FuseCallbacks.DestroyCallback> destroy = func(FuseCallbacks.DestroyCallback.class);
    public final Func<FuseCallbacks.AccessCallback> access = func(FuseCallbacks.AccessCallback.class);
    public final Func<FuseCallbacks.CreateCallback> create = func(FuseCallbacks.CreateCallback.class);
    public final Func<FuseCallbacks.FtruncateCallback> ftruncate = func(FuseCallbacks.FtruncateCallback.class);
    public final Func<FuseCallbacks.FgetattrCallback> fgetattr = func(FuseCallbacks.FgetattrCallback.class);
    public final Func<FuseCallbacks.LockCallback> lock = func(FuseCallbacks.LockCallback.class);
    public final Func<FuseCallbacks.UtimensCallback> utimens = func(FuseCallbacks.UtimensCallback.class);
    public final Func<FuseCallbacks.BmapCallback> bmap = func(FuseCallbacks.BmapCallback.class);
    /**
     * Flag indicating that the filesystem can accept a NULL path
     * as the first argument for the following operations:
     * <p>
     * read, write, flush, release, fsync, readdir, releasedir,
     * fsyncdir, ftruncate, fgetattr, lock, ioctl and poll
     * <p>
     * If this flag is set these operations continue to work on
     * unlinked files even if "-ohard_remove" option was specified.
     */
    public final Padding flag_nullpath_ok = new Padding(NativeType.UCHAR, 1);
    /**
     * Flag indicating that the path need not be calculated for
     * the following operations:
     * <p>
     * read, write, flush, release, fsync, readdir, releasedir,
     * fsyncdir, ftruncate, fgetattr, lock, ioctl and poll
     * <p>
     * Closely related to flag_nullpath_ok, but if this flag is
     * set then the path will not be calculaged even if the file
     * wasn't unlinked.  However the path can still be non-NULL if
     * it needs to be calculated for some other reason.
     */
    public final Padding flag_nopath = new Padding(NativeType.UCHAR, 1);
    /**
     * Flag indicating that the filesystem accepts special
     * UTIME_NOW and UTIME_OMIT values in its utimens operation.
     */
    public final Padding flag_utime_omit_ok = new Padding(NativeType.UCHAR, 1);
    /**
     * Reserved flags, don't set
     */
    public final Padding flag_reserved = new Padding(NativeType.UCHAR, 1);
    public final Func<FuseCallbacks.IoctlCallback> ioctl = func(FuseCallbacks.IoctlCallback.class);
    public final Func<FuseCallbacks.PollCallback> poll = func(FuseCallbacks.PollCallback.class);
    public final Func<FuseCallbacks.WritebufCallback> write_buf = func(FuseCallbacks.WritebufCallback.class);
    public final Func<FuseCallbacks.ReadbufCallback> read_buf = func(FuseCallbacks.ReadbufCallback.class);
    public final Func<FuseCallbacks.FlockCallback> flock = func(FuseCallbacks.FlockCallback.class);
    public final Func<FuseCallbacks.FallocateCallback> fallocate = func(FuseCallbacks.FallocateCallback.class);

    {
        if (Platform.IS_MAC) {
            // TODO implement MAC-OS specific functions
            for (int i = 0; i < 13; i++) {
                func(FuseCallbacks.FallocateCallback.class);
            }
        }
    }
}
