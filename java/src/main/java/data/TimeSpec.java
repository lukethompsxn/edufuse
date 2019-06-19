package data;

public class TimeSpec {
    private long sec;
    private long nsec;

    public TimeSpec(long sec, long nsec) {
        this.sec = sec;
        this.nsec = nsec;
    }

    public long getSec() {
        return sec;
    }

    public long getNsec() {
        return nsec;
    }
}
