package com.edufuse;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;

/**
 * If method marked this annotation it would'n be registered in FuseOperation struct.
 * All method in {@link com.edufuse.FuseStubFS} marked this annotation.
 *
 * This annotation is not inheritable, so all overridden method would be registered in FuseOperation.
 *
 * The goal of this annotation is performance, if method not registered in FuseOperation
 * then native->java call wouldn't be performed.
 *
 * @author Sergey Tselovalnikov
 * @since 05.06.15
 * Retrieved from https://github.com/SerCeMan/jnr-fuse
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(value = METHOD)
public @interface NotImplemented {
}
