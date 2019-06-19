package data;

public class StatVFS {
    private long bsize;
    private long frsize;
    private long blocks;
    private long bavail;
    private long files;
    private long ffree;
    private long favail;
    private long fsid;
    private long flag;
    private long namemax;

    public StatVFS(long bsize, long frsize, long blocks, long bavail, long files, long ffree, long favail, long fsid, long flag, long namemax) {
        this.bsize = bsize;
        this.frsize = frsize;
        this.blocks = blocks;
        this.bavail = bavail;
        this.files = files;
        this.ffree = ffree;
        this.favail = favail;
        this.fsid = fsid;
        this.flag = flag;
        this.namemax = namemax;
    }

    public long getBsize() {
        return bsize;
    }

    public long getFrsize() {
        return frsize;
    }

    public long getBlocks() {
        return blocks;
    }

    public long getBavail() {
        return bavail;
    }

    public long getFiles() {
        return files;
    }

    public long getFfree() {
        return ffree;
    }

    public long getFavail() {
        return favail;
    }

    public long getFsid() {
        return fsid;
    }

    public long getFlag() {
        return flag;
    }

    public long getNamemax() {
        return namemax;
    }
}
