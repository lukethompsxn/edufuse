package com.github.lukethompsxn.edufuse.struct;

import jnr.ffi.BaseStruct;

/**
 * @author Sergey Tselovalnikov
 * @see "fuse_lowlevel.c"
 * <p>
 * <pre>
 * struct fuse_pollhandle {
 *   uint64_t kh;
 *   struct fuse_chan *ch;
 *   struct fuse_ll *f;
 * };
 * </pre>
 * @since 02.06.15
 * Retrieved from https://github.com/SerCeMan/jnr-fuse
 */
public class FusePollhandle extends BaseStruct {
    protected FusePollhandle(jnr.ffi.Runtime runtime) {
        super(runtime);
    }

    public final Unsigned64 kh = new Unsigned64();
    // TODO struct fuse_chan *ch;
    public final Pointer ch = new Pointer();
    // TODO struct fuse_ll *f;
    public final Pointer f = new Pointer();

    public static FusePollhandle of(jnr.ffi.Pointer pointer) {
        FusePollhandle ph = new FusePollhandle(jnr.ffi.Runtime.getSystemRuntime());
        ph.useMemory(pointer);
        return ph;
    }
}
