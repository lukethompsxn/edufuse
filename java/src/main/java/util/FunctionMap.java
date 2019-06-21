package util;

import java.util.HashSet;

public class FunctionMap extends HashSet<FUSEOperations> {

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
        return contains(operation);
    }
}
