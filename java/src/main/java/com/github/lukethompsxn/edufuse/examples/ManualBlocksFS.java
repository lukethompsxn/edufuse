package com.github.lukethompsxn.edufuse.examples;

import com.github.lukethompsxn.edufuse.examples.util.FSVisualiser;
import com.github.lukethompsxn.edufuse.examples.util.MockINode;
import com.github.lukethompsxn.edufuse.examples.util.MockINodeTable;
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


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author Luke Thompson
 * @since 14.08.19
 */
public class ManualBlocksFS extends FileSystemStub {

    public static final int BLOCK_SIZE = 32; //bytes
    private static final String BLOCK_FILE_LOCATION = "/tmp/.blockfile"; //todo remove hardcode
    private static final String INODE_LOCATION = "/tmp/.inodetable"; //todo remove hardcode

    private static final String HELLO_PATH = "/hello";
    private static final String HELLO_STR = "Hello World!";

    private static File blockFile = null;
    private static File iNodeFile = null;

    private MockINodeTable iNodeTable = null;

    private FSVisualiser visualiser;

    @Override
    public Pointer init(Pointer conn) {
        blockFile = new File(BLOCK_FILE_LOCATION);
        iNodeFile = new File(INODE_LOCATION);

        try {
            if (!iNodeFile.exists()) {
                iNodeFile.createNewFile();
                iNodeTable = new MockINodeTable(BLOCK_SIZE);

                // setup an example file for testing purposes
                MockINode mockINode = new MockINode();
                FileStat stat = new FileStat(Runtime.getSystemRuntime());
                stat.st_mode.set(FileStat.S_IFREG | 0444 | 0222);
                stat.st_size.set(HELLO_STR.getBytes().length);
                stat.st_nlink.set(1);
                mockINode.setStat(stat);
                iNodeTable.updateINode(HELLO_PATH, mockINode);
                List<Integer> blocks = new ArrayList<>();
                blocks.add(iNodeTable.nextFreeBlock());
                mockINode.addBlocks(blocks);

                try (FileOutputStream stream = new FileOutputStream(blockFile)) {
                    stream.write(HELLO_STR.getBytes());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                iNodeTable = MockINodeTable.deserialise(iNodeFile);
            }

            if (iNodeTable == null) {
                iNodeTable = new MockINodeTable(BLOCK_SIZE);
            }

            if (!blockFile.exists()) {
                blockFile.createNewFile();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (isVisualised()) {
            visualiser = new FSVisualiser();
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
        if (!iNodeTable.containsINode(path)) {
            mknod(path, FileStat.S_IFREG, 0);
        }
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
            MockINode mockINode = iNodeTable.getINode(path);
            int fileSize = mockINode.getStat().st_size.intValue();

            int buffOffset = 0;
            try (RandomAccessFile raf = new RandomAccessFile(blockFile, "r")) {
                for (Integer block : mockINode.getBlocks()) {
                    raf.seek(block * BLOCK_SIZE);
                    byte[] bytes = new byte[(int) size];
                    raf.read(bytes);
                    buf.put(buffOffset * BLOCK_SIZE, bytes, 0, BLOCK_SIZE);
                    buffOffset++;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

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
            MockINode mockINode = iNodeTable.getINode(path);
            List<Integer> blocks = mockINode.getBlocks();

            int blockPointer = (int) Math.floorDiv(offset, (long) BLOCK_SIZE);
            int blockOffset = (int) offset % BLOCK_SIZE;
            int bufPointer = blockOffset;

            try (RandomAccessFile raf = new RandomAccessFile(blockFile, "rw")) {

                // write remaining bytes in the initial block
                if (blockPointer < blocks.size()) {
                    int numBytes = (int) Math.min(BLOCK_SIZE, size);
                    byte[] bytes = new byte[numBytes];
                    buf.get(0, bytes, 0, numBytes);
                    raf.seek((blocks.get(blockPointer) * BLOCK_SIZE) + blockOffset);
                    raf.write(bytes);
                    blockPointer++;
                    bufPointer += BLOCK_SIZE - blockOffset;
                }

                // write remaining blocks
                while (blockPointer < blocks.size() && bufPointer < size) {
                    bufPointer += write(raf, blocks, buf, size, blockPointer, bufPointer);
                    blockPointer++;
                }

                // write new blocks if needed
                while (bufPointer < size) {
                    blocks.add(iNodeTable.nextFreeBlock());
                    bufPointer += write(raf, blocks, buf, size, blockPointer, bufPointer);
                    blockPointer++;
                }

                FileStat stat = iNodeTable.getINode(path).getStat();
                stat.st_size.set(size + offset);
                MockINodeTable.serialise(iNodeFile, iNodeTable);

                if (isVisualised()) {
                    visualiser.sendINodeTable(iNodeTable);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return (int) size;
    }

    @Override
    public int mknod(String path, @mode_t long mode, @dev_t long rdev) {
        if (iNodeTable.containsINode(path)) {
            return -ErrorCodes.EEXIST();
        }

        MockINode mockINode = new MockINode();
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
        ManualBlocksFS fs = new ManualBlocksFS();
        try {
            fs.mount(args, true);
        } finally {
            fs.unmount();
        }
    }

    private int write(RandomAccessFile raf, List<Integer> blocks, Pointer buf, @size_t long size, int blockPointer, int bufPointer) throws IOException {
        int numBytes = Math.min(BLOCK_SIZE, (int) size - bufPointer);
        byte[] bytes = new byte[numBytes];
        buf.get(bufPointer, bytes, 0, numBytes);
        raf.seek((blocks.get(blockPointer) * BLOCK_SIZE));
        raf.write(bytes);
        return numBytes;
    }
}
