package data;

public class Stat {
    private long dev;
    private int pad1;
    private long ino;
    private int mode;
    private long nlink;
    private int uid;
    private int gid;
    private int pad0;
    private long rdev;
    private int pad2;
    private long size;
    private int blksize;
    private long blocks;

    public Stat(long dev, int pad1, long ino, int mode, long nlink, int uid, int gid, int pad0, long rdev, int pad2, long size, int blksize, long blocks) {
        this.dev = dev;
        this.pad1 = pad1;
        this.ino = ino;
        this.mode = mode;
        this.nlink = nlink;
        this.uid = uid;
        this.gid = gid;
        this.pad0 = pad0;
        this.rdev = rdev;
        this.pad2 = pad2;
        this.size = size;
        this.blksize = blksize;
        this.blocks = blocks;
    }

    public long getDev() {
        return dev;
    }

    public int getPad1() {
        return pad1;
    }

    public long getIno() {
        return ino;
    }

    public int getMode() {
        return mode;
    }

    public long getNlink() {
        return nlink;
    }

    public int getUid() {
        return uid;
    }

    public int getGid() {
        return gid;
    }

    public int getPad0() {
        return pad0;
    }

    public long getRdev() {
        return rdev;
    }

    public int getPad2() {
        return pad2;
    }

    public long getSize() {
        return size;
    }

    public int getBlksize() {
        return blksize;
    }

    public long getBlocks() {
        return blocks;
    }
}
