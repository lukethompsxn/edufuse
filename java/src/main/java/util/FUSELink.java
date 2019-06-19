package util;

public class FUSELink {
    private native void registerOperations();

    public void call() {
        new FUSELink().registerOperations();
    }

    static {
        System.loadLibrary("FUSELink");
    }
}
