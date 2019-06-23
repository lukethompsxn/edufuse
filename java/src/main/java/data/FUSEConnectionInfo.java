package data;

public class FUSEConnectionInfo {
    private int proto_major;
    private int proto_minor;
    private int async_read;
    private int max_write;
    private int max_readahead;
    private int capable;
    private int want;
    private int congestion_threshhold;

    public FUSEConnectionInfo(int proto_major, int proto_minor, int async_read, int max_write, int max_readahead, int capable, int want, int congestion_threshhold) {
        this.proto_major = proto_major;
        this.proto_minor = proto_minor;
        this.async_read = async_read;
        this.max_write = max_write;
        this.max_readahead = max_readahead;
        this.capable = capable;
        this.want = want;
        this.congestion_threshhold = congestion_threshhold;
    }

    public int getProtoMajor() {
        return proto_major;
    }

    public int getProtoMinor() {
        return proto_minor;
    }

    public int getAsyncRead() {
        return async_read;
    }

    public int getMaxWrite() {
        return max_write;
    }

    public int getMaxReadahead() {
        return max_readahead;
    }

    public int getCapable() {
        return capable;
    }

    public int getWant() {
        return want;
    }

    public int getCongestionThreshhold() {
        return congestion_threshhold;
    }
}
