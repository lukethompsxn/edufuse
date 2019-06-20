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
        return containsKey(FUSEOperations.valueOf(key));
    }
}
