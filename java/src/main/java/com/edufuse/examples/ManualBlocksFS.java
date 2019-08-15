package com.edufuse.examples;

import com.edufuse.filesystem.FileSystemStub;
import com.edufuse.struct.*;
import com.edufuse.util.ErrorCodes;
import com.edufuse.util.FuseFillDir;
import jnr.ffi.Pointer;
import jnr.ffi.Runtime;
import jnr.ffi.Struct;
import jnr.ffi.Struct.NumberField;
import jnr.ffi.types.dev_t;
import jnr.ffi.types.mode_t;
import jnr.ffi.types.off_t;
import jnr.ffi.types.size_t;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

/**
 * @author Luke Thompson
 * @since 14.08.19
 */
public class ManualBlocksFS extends FileSystemStub {

    private static final int BLOCK_SIZE = 32; //bytes
    private static final String BLOCK_FILE_LOCATION = "/tmp/.blockfile"; //todo remove hardcode
    //    private static final String INODE_LOCATION = "/tmp/.inode"; //todo remove hardcode

    private static final String HELLO_PATH = "/hello";
    private static final String HELLO_STR = "Hello World!";

    private static File blockFile = null;
//    private static File inode = null;
    private HashMap<String, INode> inodeTable = new HashMap<>();
    private int blockIndex = 1; //todo this is terrible lmao fix it by encapsulating the inode table into object, will also make json easier

    @Override
    public Pointer init(Pointer conn) {
        blockFile = new File(BLOCK_FILE_LOCATION);
//        inode = new File(INODE_LOCATION);

        //todo implement persistence
        try {
//            if (!inode.exists()) {
//                inode.createNewFile();
//            }
//
            if (!blockFile.exists()) {
                blockFile.createNewFile();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        INode iNode = new INode();
        FileStat stat = new FileStat(Runtime.getSystemRuntime());
        stat.st_mode.set(FileStat.S_IFREG | 0444);
        stat.st_size.set(HELLO_STR.getBytes().length);
        stat.st_nlink.set(1);
        iNode.setStat(stat);
        inodeTable.put(HELLO_PATH, iNode);

        try (FileOutputStream stream = new FileOutputStream(blockFile)){
            stream.write(HELLO_STR.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
        blockIndex++;

        return conn;
    }

    @Override
    public int getattr(String path, FileStat stat) {
        int res = 0;
        if (Objects.equals(path, "/")) {
            stat.st_mode.set(FileStat.S_IFDIR | 0755);
            stat.st_nlink.set(2);
        } else if (inodeTable.containsKey(path)) {
            stat.st_mode.set(FileStat.S_IFREG | 0444);
            stat.st_nlink.set(1);
            stat.st_size.set(inodeTable.get(path).getStat().st_size.intValue());
        } else {
            res = -ErrorCodes.ENOENT();
        }
        return res;
    }

    @Override
    public int readdir(String path, Pointer buf, FuseFillDir filler, @off_t long offset, FuseFileInfo fi) {
        filler.apply(buf, ".", null, 0);
        filler.apply(buf, "..", null, 0);

        for (String file : inodeTable.keySet()) {
            filler.apply(buf, file.substring(1), inodeTable.get(file).getStat(), 0);
        }
        return 0;
    }

    @Override
    public int open(String path, FuseFileInfo fi) {
        //todo this may need re-directing through the dedicated file but i think we fine to just return 0
        if (!inodeTable.containsKey(path)) {
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
        if (!inodeTable.containsKey(path)) {
            return -ErrorCodes.ENONET();
        }

        synchronized (this) {
            INode iNode = inodeTable.get(path);
            int fileSize = iNode.getStat().st_size.intValue();

            int buffOffset = 0;
            try (FileInputStream inputStream = new FileInputStream(blockFile)) {
                FileChannel channel = inputStream.getChannel();
                for (Integer block : iNode.getBlocks()) {
                    channel.position(block * BLOCK_SIZE);
                    ByteBuffer buffer = ByteBuffer.allocate(BLOCK_SIZE);
                    channel.read(buffer);
                    buf.put(buffOffset * BLOCK_SIZE, buffer.array(), 0, BLOCK_SIZE);
                    buffOffset++;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return fileSize;
        }
    }

    @Override
    public int write(String path, Pointer buf, @size_t long size, @off_t long offset, FuseFileInfo fi) {
        if (!inodeTable.containsKey(path)) {
            return -ErrorCodes.ENONET();
        }

        synchronized (this) {
            INode iNode = inodeTable.get(path);
            List<Integer> blocks = iNode.getBlocks();

            int blockPointer = (int) Math.floorDiv(offset, (long) BLOCK_SIZE);
            int blockOffset = (int) offset % BLOCK_SIZE;

            try (FileOutputStream outputStream = new FileOutputStream(blockFile)){
                FileChannel channel = outputStream.getChannel();

                if (blockPointer < blocks.size()) {
                    // write remaining bytes in the initial block
                    byte[] bytes = new byte[BLOCK_SIZE - blockOffset];
                    buf.get(0, bytes, 0, BLOCK_SIZE - blockOffset);
                    channel.position((blocks.get(blockPointer) * BLOCK_SIZE) + blockOffset);
                    ByteBuffer buffer = ByteBuffer.wrap(bytes);
                    channel.write(buffer);
                }

                // write remaining blocks
                blockPointer++;
                int bufPointer = BLOCK_SIZE - blockOffset;
                while (blockPointer < blocks.size() && bufPointer < size) {
                    bufPointer += write(channel, blocks, buf, size, blockPointer, bufPointer);
                    blockPointer++;
                }

                // write new blocks if needed
                while (bufPointer < size) {
                    blocks.add(blockPointer + 1, blockIndex);
                    bufPointer += write(channel, blocks, buf, size, blockPointer, bufPointer);
                    blockPointer++;
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        return (int) ((int) size-offset);
    }

    @Override
    public int mknod(String path, @mode_t long mode, @dev_t long rdev) {
        if (inodeTable.containsKey(path)) {
            return -ErrorCodes.EEXIST();
        }

        INode iNode = new INode();
        FileStat stat = new FileStat(Runtime.getSystemRuntime());
        stat.st_mode.set(mode);
        stat.st_rdev.set(rdev);
        iNode.setStat(stat);
        inodeTable.put(path, iNode);

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
        return super.mkdir(path, mode);
    }

    @Override
    public int rmdir(String path) {
        return super.rmdir(path);
    }

    @Override
    public int rename(String oldpath, String newpath) {
        return super.rename(oldpath, newpath);
    }

    @Override
    public int truncate(String path, long size) {
        return super.truncate(path, size);
    }

    @Override
    public int release(String path, FuseFileInfo fi) {
        return 0;
    }

    @Override
    public int fsync(String path, int isdatasync, FuseFileInfo fi) {
        return super.fsync(path, isdatasync, fi);
    }

    @Override
    public int setxattr(String path, String name, Pointer value, long size, int flags) {
        return super.setxattr(path, name, value, size, flags);
    }

    @Override
    public int getxattr(String path, String name, Pointer value, long size) {
        return super.getxattr(path, name, value, size);
    }

    @Override
    public int listxattr(String path, Pointer list, long size) {
        return super.listxattr(path, list, size);
    }

    @Override
    public int removexattr(String path, String name) {
        return super.removexattr(path, name);
    }

    @Override
    public int opendir(String path, FuseFileInfo fi) {
        return super.opendir(path, fi);
    }

    @Override
    public int releasedir(String path, FuseFileInfo fi) {
        return super.releasedir(path, fi);
    }

    @Override
    public int fsyncdir(String path, FuseFileInfo fi) {
        return super.fsyncdir(path, fi);
    }

    @Override
    public void destroy(Pointer initResult) {
        super.destroy(initResult);
    }

    @Override
    public int access(String path, int mask) {
        return super.access(path, mask);
    }

    @Override
    public int ftruncate(String path, long size, FuseFileInfo fi) {
        return super.ftruncate(path, size, fi);
    }

    @Override
    public int fgetattr(String path, FileStat stbuf, FuseFileInfo fi) {
        return super.fgetattr(path, stbuf, fi);
    }

    @Override
    public int lock(String path, FuseFileInfo fi, int cmd, Flock flock) {
        return super.lock(path, fi, cmd, flock);
    }

    @Override
    public int flock(String path, FuseFileInfo fi, int op) {
        return super.flock(path, fi, op);
    }

    @Override
    public int fallocate(String path, int mode, long off, long length, FuseFileInfo fi) {
        return super.fallocate(path, mode, off, length, fi);
    }

    public static void main(String[] args) {
        ManualBlocksFS fs = new ManualBlocksFS();
        try {
            fs.mount(args, false);
        } finally {
            fs.unmount();
        }
    }

    private int write(FileChannel channel, List<Integer> blocks, Pointer buf, long size, int blockPointer, int bufPointer) throws IOException {
        int numBytes = Math.min(BLOCK_SIZE, (int) size - bufPointer);
        byte[] bytes = new byte[numBytes];
        buf.get(bufPointer, bytes, 0, numBytes);
        channel.position((blocks.get(blockPointer) * BLOCK_SIZE));
        ByteBuffer buffer = ByteBuffer.wrap(bytes);
        channel.write(buffer);
        return numBytes;
    }

    class INode {
        private FileStat stat;
        private List<Integer> blocks = new ArrayList<>();

        public FileStat getStat() {
            return stat;
        }

        public void setStat(FileStat stat) {
            this.stat = stat;
        }

        public void addBlocks(List<Integer> offsets) {
            blocks.addAll(offsets);
        }

        public List<Integer> getBlocks() {
            return blocks;
        }
    }
}
