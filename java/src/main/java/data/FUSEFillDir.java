package data;

public class FUSEFillDir {
    private String name;
    private Stat stat;
    private long off;

    public FUSEFillDir(String name, Stat stat, long off) {
        this.name = name;
        this.stat = stat;
        this.off = off;
    }

    public String getName() {
        return name;
    }

    public Stat getStat() {
        return stat;
    }

    public long getOff() {
        return off;
    }
}
