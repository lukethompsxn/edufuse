package com.github.lukethompsxn.edufuse.examples;

import com.github.lukethompsxn.edufuse.examples.util.MemoryINode;
import com.github.lukethompsxn.edufuse.examples.util.MemoryINodeTable;
import com.github.lukethompsxn.edufuse.examples.util.MemoryVisualiser;
import com.github.lukethompsxn.edufuse.filesystem.FileSystemStub;
import com.github.lukethompsxn.edufuse.struct.*;
import com.github.lukethompsxn.edufuse.util.ErrorCodes;
import com.github.lukethompsxn.edufuse.util.FuseFillDir;
import jnr.ffi.Pointer;
import jnr.ffi.Runtime;
import jnr.ffi.types.dev_t;
import jnr.ffi.types.mode_t;
import jnr.ffi.types.off_t;
import jnr.ffi.types.size_t;

import java.io.IOException;
import java.util.Objects;

/**
 * @author Luke Thompson
 * @since 14.08.19
 */
public class MemoryFS extends FileSystemStub {
    private static final String HELLO_PATH = "/hello";
    private static final String HELLO_STR = "Hello World!\n";

    private MemoryINodeTable iNodeTable = new MemoryINodeTable();
    private MemoryVisualiser visualiser;

    @Override
    public Pointer init(Pointer conn) {


        // setup an example file for testing purposes
        MemoryINode iNode = new MemoryINode();
        FileStat stat = new FileStat(Runtime.getSystemRuntime());
        stat.st_mode.set(FileStat.S_IFREG | 0444 | 0222);
        stat.st_size.set(HELLO_STR.getBytes().length);
        stat.st_nlink.set(1);
        iNode.setStat(stat);
        iNode.setContent(HELLO_STR.getBytes());
        iNodeTable.updateINode(HELLO_PATH, iNode);

        if (isVisualised()) {
            visualiser = new MemoryVisualiser();
            visualiser.sendINodeTable(iNodeTable);
        }

        return conn;
    }

    @Override
    public int getattr(String path, FileStat stat) {
        int res = 0;
        if (Objects.equals(path, "/")) {
            stat.st_mode.set(FileStat.S_IFDIR | 0755);
            stat.st_nlink.set(2);
        } else if (iNodeTable.containsINode(path)) {
            FileStat savedStat = iNodeTable.getINode(path).getStat();
            stat.st_mode.set(savedStat.st_mode.intValue());
            stat.st_nlink.set(savedStat.st_nlink.intValue());
            stat.st_size.set(iNodeTable.getINode(path).getStat().st_size.intValue());
        } else {
            res = -ErrorCodes.ENOENT();
        }
        return res;
    }

    @Override
    public int readdir(String path, Pointer buf, FuseFillDir filler, @off_t long offset, FuseFileInfo fi) {
        filler.apply(buf, ".", null, 0);
        filler.apply(buf, "..", null, 0);

        for (String file : iNodeTable.entires()) {
            filler.apply(buf, file.substring(1), iNodeTable.getINode(file).getStat(), 0);
        }
        return 0;
    }

    @Override
    public int open(String path, FuseFileInfo fi) {
//        if (!iNodeTable.containsINode(path)) {
//            mknod(path, FileStat.S_IFREG, 0);
//        }
        return 0;
    }

    @Override
    public int flush(String path, FuseFileInfo fi) {
        return 0;
    }

    @Override
    public int read(String path, Pointer buf, @size_t long size, @off_t long offset, FuseFileInfo fi) {
        if (!iNodeTable.containsINode(path)) {
            return -ErrorCodes.ENONET();
        }

        synchronized (this) {
            MemoryINode mockINode = iNodeTable.getINode(path);
            int fileSize = mockINode.getStat().st_size.intValue();
            buf.put(0, mockINode.getContent(), 0, mockINode.getContent().length);

            if (isVisualised()) {
                visualiser.sendINodeTable(iNodeTable);
            }

            return fileSize;
        }
    }

    @Override
    public int write(String path, Pointer buf, @size_t long size, @off_t long offset, FuseFileInfo fi) {
        if (!iNodeTable.containsINode(path)) {
            return -ErrorCodes.ENONET();
        }

        synchronized (this) {
            MemoryINode mockINode = iNodeTable.getINode(path);

            byte[] content = mockINode.getContent();

            if (offset + size > content.length) {
                byte[] newContent = new byte[(int) (offset + size)];
                FileStat stat = mockINode.getStat();
                stat.st_size.set(offset + size);

                System.arraycopy(content, 0, newContent, 0, content.length);
                content = newContent;
            }

            for (long i = offset; i < offset + size; i++) {
                content[(int) i] = buf.getByte(i);
            }

            mockINode.setContent(content);
        }

        if (isVisualised()) {
            visualiser = new MemoryVisualiser();
            visualiser.sendINodeTable(iNodeTable);
        }

        return (int) size;
    }

    @Override
    public int mknod(String path, @mode_t long mode, @dev_t long rdev) {
        if (iNodeTable.containsINode(path)) {
            return -ErrorCodes.EEXIST();
        }

        MemoryINode mockINode = new MemoryINode();
        FileStat stat = new FileStat(Runtime.getSystemRuntime());
        stat.st_mode.set(mode);
        stat.st_rdev.set(rdev);
        mockINode.setStat(stat);

        iNodeTable.updateINode(path, mockINode);

        if (isVisualised()) {
            visualiser.sendINodeTable(iNodeTable);
        }

        return 0;
    }

    @Override
    public int statfs(String path, Statvfs stbuf) {
        return super.statfs(path, stbuf);
    }

    @Override
    public int utimens(String path, Timespec[] timespec) {
        timespec[0].tv_nsec.set(System.nanoTime());
        timespec[0].tv_sec.set(System.currentTimeMillis() / 100);
        timespec[1].tv_nsec.set(System.nanoTime());
        timespec[1].tv_sec.set(System.currentTimeMillis() / 100);
        return 0;
    }

    @Override
    public int unlink(String path) {
        if (!iNodeTable.containsINode(path)) {
            return -ErrorCodes.ENONET();
        }

        synchronized (this) {
            iNodeTable.removeINode(path);
        }

        return 0;
    }

    @Override
    public int mkdir(String path, long mode) {
        return 0;
    }

    @Override
    public int rmdir(String path) {
        return 0;
    }

    @Override
    public int rename(String oldpath, String newpath) {
        return 0;
    }

    @Override
    public int truncate(String path, @size_t long size) {
        return 0;
    }

    @Override
    public int release(String path, FuseFileInfo fi) {
        return 0;
    }

    @Override
    public int fsync(String path, int isdatasync, FuseFileInfo fi) {
        return 0;
    }

    @Override
    public int setxattr(String path, String name, Pointer value, @size_t long size, int flags) {
        return 0;
    }

    @Override
    public int getxattr(String path, String name, Pointer value, @size_t long size) {
        return 0;
    }

    @Override
    public int listxattr(String path, Pointer list, @size_t long size) {
        return 0;
    }

    @Override
    public int removexattr(String path, String name) {
        return 0;
    }

    @Override
    public int opendir(String path, FuseFileInfo fi) {
        return 0;
    }

    @Override
    public int releasedir(String path, FuseFileInfo fi) {
        return 0;
    }

    @Override
    public void destroy(Pointer initResult) {
        if (isVisualised()) {
            try {
                visualiser.stopConnection();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public int access(String path, int mask) {
        return 0;
    }

    @Override
    public int lock(String path, FuseFileInfo fi, int cmd, Flock flock) {
        return 0;
    }

    public static void main(String[] args) {
        MemoryFS fs = new MemoryFS();
        try {
            fs.mount(args, true);
        } finally {
            fs.unmount();
        }
    }
}
