package data;

public class Flock {
    private int type;
    private int whence;
    private long start;
    private long len;
    private int pid;

    public Flock(int type, int whence, long start, long len, int pid) {
        this.type = type;
        this.whence = whence;
        this.start = start;
        this.len = len;
        this.pid = pid;
    }

    public int getType() {
        return type;
    }

    public int getWhence() {
        return whence;
    }

    public long getStart() {
        return start;
    }

    public long getLen() {
        return len;
    }

    public int getPid() {
        return pid;
    }
}
