package com.edufuse.examples;

import com.edufuse.ErrorCodes;
import com.edufuse.FuseFillDir;
import com.edufuse.FuseStubFS;
import com.edufuse.struct.FileStat;
import com.edufuse.struct.FuseFileInfo;
import jnr.ffi.Pointer;
import jnr.ffi.types.off_t;
import jnr.ffi.types.size_t;

import java.util.Objects;

/**
 * @author Sergey Tselovalnikov
 * @see <a href="http://fuse.sourceforge.net/helloworld.html">helloworld</a>
 * @since 31.05.15
 * Retrieved from https://github.com/SerCeMan/jnr-fuse
 */
public class HelloFuse extends FuseStubFS {

    public static final String HELLO_PATH = "/hello";
    public static final String HELLO_STR = "Hello World!";

    @Override
    public int getattr(String path, FileStat stat) {
        int res = 0;
        if (Objects.equals(path, "/")) {
            stat.st_mode.set(FileStat.S_IFDIR | 0755);
            stat.st_nlink.set(2);
        } else if (HELLO_PATH.equals(path)) {
            stat.st_mode.set(FileStat.S_IFREG | 0444);
            stat.st_nlink.set(1);
            stat.st_size.set(HELLO_STR.getBytes().length);
        } else {
            res = -ErrorCodes.ENOENT();
        }
        return res;
    }

    @Override
    public int readdir(String path, Pointer buf, FuseFillDir filter, @off_t long offset, FuseFileInfo fi) {
        if (!"/".equals(path)) {
            return -ErrorCodes.ENOENT();
        }

        filter.apply(buf, ".", null, 0);
        filter.apply(buf, "..", null, 0);
        filter.apply(buf, HELLO_PATH.substring(1), null, 0);
        return 0;
    }

    @Override
    public int open(String path, FuseFileInfo fi) {
        if (!HELLO_PATH.equals(path)) {
            return -ErrorCodes.ENOENT();
        }
        return 0;
    }

    @Override
    public int read(String path, Pointer buf, @size_t long size, @off_t long offset, FuseFileInfo fi) {
        if (!HELLO_PATH.equals(path)) {
            return -ErrorCodes.ENOENT();
        }

        byte[] bytes = HELLO_STR.getBytes();
        int length = bytes.length;
        if (offset < length) {
            if (offset + size > length) {
                size = length - offset;
            }
            buf.put(0, bytes, 0, bytes.length);
        } else {
            size = 0;
        }
        return (int) size;
    }

    public static void main(String[] args) {
        HelloFuse stub = new HelloFuse();
        try {
//            String path;
//            switch (Platform.getNativePlatform().getOS()) {
//                case WINDOWS:
//                    path = "J:\\";
//                    break;
//                default:
//                    path = "/tmp/mnth";
//            }
            stub.mount(args, false);
        } finally {
            stub.unmount();
        }
    }
}
