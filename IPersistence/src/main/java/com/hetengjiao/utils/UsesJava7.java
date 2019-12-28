package com.hetengjiao.utils;

import java.lang.annotation.*;

/**
 * <p>
 * Indicates that the element uses Java 7 API.
 * </p>
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE, ElementType.METHOD, ElementType.FIELD })
public @interface UsesJava7 {
}