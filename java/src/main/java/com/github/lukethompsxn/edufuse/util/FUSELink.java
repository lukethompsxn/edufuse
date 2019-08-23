package com.github.lukethompsxn.edufuse.util;

import com.github.lukethompsxn.edufuse.struct.FuseBufvec;
import com.github.lukethompsxn.edufuse.struct.FuseContext;
import com.github.lukethompsxn.edufuse.struct.FuseOperations;
import jnr.ffi.types.size_t;
import jnr.ffi.types.ssize_t;

/**
 * @author Luke Thompson
 * @since 20/7/2019
 */
public interface FUSELink {
    @size_t
    long fuse_buf_size(FuseBufvec bufv);

    @ssize_t
    long fuse_buf_copy(FuseBufvec dstv, FuseBufvec srcv, int flags);

    int edufuse_register(int argc, String[] argv, FuseOperations op, int isVisualised);

    FuseContext fuse_get_context();
}
