package com.github.lukethompsxn.edufuse.util;

/**
 * Retrieved from https://github.com/SerCeMan/jnr-fuse
 */
public class FuseException extends RuntimeException {
    public FuseException(String message) {
        super(message);
    }

    public FuseException(String message, Throwable cause) {
        super(message, cause);
    }
}
