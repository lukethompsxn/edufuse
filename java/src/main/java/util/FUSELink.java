package util;

public class FUSELink {
    private native int registerOperations(FunctionMap operations, String[] argv, int isVisualised);

    /**
     * At minimum, args must contain the path of the directory being mounted.
     */
    public void register(FunctionMap operations, boolean isVisualised, String[] args) {
        new FUSELink().registerOperations(operations, args, isVisualised ? 1 : 0);
    }

    static {
        System.loadLibrary("FUSELink");
    }
}
