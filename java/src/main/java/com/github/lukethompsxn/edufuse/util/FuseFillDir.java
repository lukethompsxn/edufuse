package com.github.lukethompsxn.edufuse.util;

import com.github.lukethompsxn.edufuse.struct.FileStat;
import jnr.ffi.Pointer;
import jnr.ffi.Struct;
import jnr.ffi.annotations.Delegate;
import jnr.ffi.types.off_t;

import java.nio.ByteBuffer;

/**
 * Function to add an entry in a readdir() operation
 * <p>
 * Retrieved from https://github.com/SerCeMan/jnr-fuse
 */
public interface FuseFillDir {
    @Delegate
    int apply(Pointer buf, ByteBuffer name, Pointer stbuf, @off_t long off);

    /**
     * Function to add an entry in a readdir() operation
     *
     * @param buf   the buffer passed to the readdir() operation
     * @param name  the file name of the directory entry
     * @param stbuf file attributes, can be NULL
     * @param off   offset of the next entry or zero
     * @return 1 if buffer is full, zero otherwise
     */
    default int apply(Pointer buf, String name, FileStat stbuf, @off_t long off) {
        return apply(buf, ByteBuffer.wrap(name.getBytes()), stbuf == null ? null : Struct.getMemory(stbuf), off);
    }
}
