package data;

public class FUSEFileInfo {
    private int flags;
    private int writepage;
    private boolean direct_io;
    private boolean keep_cache;
    private boolean flush;
    private boolean nonseekable;
    private boolean flock_release;
    private int padding;
    private long fh;
    private long lock_owner;

    public FUSEFileInfo(int flags, int writepage, Boolean direct_io, Boolean keep_cache, Boolean flush, Boolean nonseekable, Boolean flock_release, Integer padding, long fh, long lock_owner) {
        this.flags = flags;
        this.writepage = writepage;
        this.direct_io = direct_io != null ? direct_io : true;
        this.keep_cache = keep_cache != null ? keep_cache : true;
        this.flush = flush != null ? flush : true;
        this.nonseekable = nonseekable != null ? nonseekable : true;
        this.flock_release = flock_release != null ? flock_release : true;
        this.padding = padding != null ? padding : 27;
        this.fh = fh;
        this.lock_owner = lock_owner;
    }

    public int getFlags() {
        return flags;
    }

    public int getWritepage() {
        return writepage;
    }

    public boolean isDirectIO() {
        return direct_io;
    }

    public boolean isKeepCache() {
        return keep_cache;
    }

    public boolean isFlush() {
        return flush;
    }

    public boolean isNonSeekable() {
        return nonseekable;
    }

    public boolean isFlockRelease() {
        return flock_release;
    }

    public int getPadding() {
        return padding;
    }

    public long getFH() {
        return fh;
    }

    public long getLockOwner() {
        return lock_owner;
    }

    public void setFlags(int flags) {
        this.flags = flags;
    }

    public void setDirectIO(boolean direct_io) {
        this.direct_io = direct_io;
    }

    public void setKeepCache(boolean keep_cache) {
        this.keep_cache = keep_cache;
    }

    public void setFlush(boolean flush) {
        this.flush = flush;
    }

    public void setNonSeekable(boolean nonseekable) {
        this.nonseekable = nonseekable;
    }

    public void setFlockRelease(boolean flock_release) {
        this.flock_release = flock_release;
    }
}
