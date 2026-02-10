package com.financialapplication.tijori.Config;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import org.springframework.stereotype.Component;
import java.time.Duration;
import java.util.concurrent.TimeUnit;

/**
 * Rate limiting configuration using Bucket4j.
 * Implements token bucket algorithm for protecting APIs from abuse.
 */
@Component
public class RateLimitConfig {


    private  final Cache<String,Bucket> bucketCache = Caffeine.newBuilder().
            initialCapacity(1000)
            .expireAfterWrite(1, TimeUnit.HOURS)
            .build();


    private static final int STANDARD_CAPACITY = 50;
    private static final int STANDARD_REFILL_TOKENS = 50;
    private static final Duration STANDARD_REFILL_DURATION = Duration.ofMinutes(1);

    private static final int AUTH_CAPACITY = 10;
    private static final int AUTH_REFILL_TOKENS = 10;
    private static final Duration AUTH_REFILL_DURATION = Duration.ofMinutes(1);

    /**
     * Gets or creates a rate limit bucket for a given key (e.g., IP address or user ID).
     *
     * @param key        The unique identifier for the bucket i.e. combination of clientIp and Bucket type
     * @param bucketType The type of bucket (auth or standard)
     * @return The bucket for rate limiting
     */
    public Bucket resolveBucket(String key, BucketType bucketType) {
      return bucketCache.get(key,k->createBucket(bucketType));
    }

    private Bucket createBucket(BucketType bucketType) {
        return switch (bucketType) {
            case AUTH -> createAuthBucket();
            case STANDARD -> createStandardBucket();
        };
    }

    /**
     * Creates a standard bucket for general API endpoints.
     * Allows 50 requests per minute.
     */
    private Bucket createStandardBucket() {
        Bandwidth limit = Bandwidth.builder()
                .capacity(STANDARD_CAPACITY)
                .refillGreedy(STANDARD_REFILL_TOKENS, STANDARD_REFILL_DURATION)
                .build();

        return Bucket.builder().addLimit(limit).build();
    }

    /**
     * Creates a more restrictive bucket for authentication endpoints.
     * Allows 10 requests per minute to prevent brute-force attacks.
     */
    private Bucket createAuthBucket() {
        Bandwidth limit =  Bandwidth.builder()
                .capacity(AUTH_CAPACITY)
                .refillIntervally(AUTH_REFILL_TOKENS,AUTH_REFILL_DURATION)
                .build();
        return Bucket.builder().addLimit(limit).build();
    }

    public enum BucketType {
        AUTH,
        STANDARD
    }
}

