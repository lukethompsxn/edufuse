package com.github.lukethompsxn.edufuse.util;

/**
 * Retrieved from https://github.com/SerCeMan/jnr-fuse
 */
public final class SecurityUtils {
    public static boolean canHandleShutdownHooks() {
        SecurityManager security = System.getSecurityManager();
        if (security == null) {
            return true;
        }
        try {
            security.checkPermission(new RuntimePermission("shutdownHooks"));
            return true;
        } catch (final SecurityException e) {
            return false;
        }
    }

    private SecurityUtils() {
    }
}
