package util;

import filesystem.AbstractFS;

import java.util.HashSet;

public class FunctionMap extends HashSet<FUSEOperations> {
    private AbstractFS filesystem;

    public FunctionMap(AbstractFS filesystem) {
        this.filesystem = filesystem;
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
        return contains(operation);
    }

    /**
     * Used through JNI, do not delete
     */
    public AbstractFS getFilesystem() {
        return filesystem;
    }
}
