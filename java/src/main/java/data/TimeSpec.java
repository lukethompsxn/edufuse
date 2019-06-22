package data;

public class TimeSpec {
    private long sec1;
    private long nsec1;
    private long sec2;
    private long nsec2;

    public TimeSpec(long sec1, long nsec1, long sec2, long nsec2) {
        this.sec1 = sec1;
        this.nsec1 = nsec1;
        this.sec2 = sec2;
        this.nsec2 = nsec2;
    }

    public long getSec1() {
        return sec1;
    }

    public long getNsec1() {
        return nsec1;
    }

    public long getSec2() {
        return sec2;
    }

    public long getNsec2() {
        return nsec2;
    }
}
