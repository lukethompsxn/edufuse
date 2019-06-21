package util;

import java.util.EnumMap;

public class FunctionMap extends EnumMap<FUSEOperations, Runnable> {

    public FunctionMap() {
        super(FUSEOperations.class);
    }

    /**
     * Used through JNI, do not delete
     */
    public boolean contains(String key) {
        FUSEOperations operation;
        try {
            operation = FUSEOperations.valueOf(key);
        } catch (IllegalArgumentException e) {
            return false;
        }
        return containsKey(operation);
    }
}
