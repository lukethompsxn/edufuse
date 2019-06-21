package util;

public class FUSELink {
    private native int registerOperations(FunctionMap operations, String[] argv);

    /**
     * At minimum, args must contain the path of the directory being mounted.
     */
    public void register(FunctionMap operations, String[] args) {
        Integer a = new FUSELink().registerOperations(operations, args);
    }

    static {
        System.loadLibrary("FUSELink");
    }
}
