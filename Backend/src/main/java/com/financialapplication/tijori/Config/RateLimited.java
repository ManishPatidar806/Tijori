package com.financialapplication.tijori.Config;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to apply rate limiting to controller methods.
 * Use this annotation to protect endpoints from abuse.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RateLimited {
    

    RateLimitConfig.BucketType bucketType() default RateLimitConfig.BucketType.STANDARD;
}
