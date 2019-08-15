package com.edufuse.examples;

import com.edufuse.filesystem.FileSystemStub;
import com.edufuse.struct.FileStat;
import com.edufuse.struct.FuseFileInfo;
import com.edufuse.util.ErrorCodes;
import com.edufuse.util.FuseFillDir;
import jnr.ffi.Pointer;
import jnr.ffi.Runtime;
import jnr.ffi.Struct;
import jnr.ffi.Struct.NumberField;
import jnr.ffi.types.mode_t;
import jnr.ffi.types.off_t;
import jnr.ffi.types.size_t;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author Luke Thompson
 * @since 14.08.19
 */
public class ManualBlocksFS extends FileSystemStub {

    private static final int BLOCK_SIZE = 32; //bytes
    private static final String BLOCK_FILE_LOCATION = "/tmp/.blockfile"; //todo remove hardcode
    //    private static final String INODE_LOCATION = "/tmp/.inode"; //todo remove hardcode

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
//        stat.st_mode.set(FileStat.S_IFREG);
        iNode.setStat(stat);
        inodeTable.put("testing file hehe", iNode);
        blockIndex++;

        return conn;
    }

    @Override
    public int getattr(String path, FileStat stat) {
        try {
            stat.st_mode.set(FileStat.S_IFDIR | 0777);
            stat.st_uid.set(getContext().uid.get());
            stat.st_gid.set(getContext().gid.get());
            return 0;
        } catch (Exception e) {
            return -ErrorCodes.ENOENT();
        }
    }

    @Override
    public int readdir(String path, Pointer buf, FuseFillDir filler, @off_t long offset, FuseFileInfo fi) {
        filler.apply(buf, ".", null, 0);
        filler.apply(buf, "..", null, 0);

        for (String file : inodeTable.keySet()) {
            filler.apply(buf, file, null, 0);
        }
        return 0;
    }

    @Override
    public int open(String path, FuseFileInfo fi) {
        //todo this may need re-directing through the dedicated file but i think we fine to just return 0
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
    public int create(String path, long mode, FuseFileInfo fi) {
        if (inodeTable.containsKey(path)) {
            return -ErrorCodes.EEXIST();
        }

        INode iNode = new INode();
        FileStat stat = new FileStat(Runtime.getSystemRuntime());
        stat.st_mode.set(mode); //todo fill in other stat info
        iNode.setStat(stat);
        inodeTable.put(path, iNode);

        return 0;
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
