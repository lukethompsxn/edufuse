package util;

public class FUSELink {
    private native int registerOperations(FunctionMap operations);

    public void register(FunctionMap operations) {
        Integer a = new FUSELink().registerOperations(operations);
    }

    static {
        System.loadLibrary("FUSELink");
    }
}
