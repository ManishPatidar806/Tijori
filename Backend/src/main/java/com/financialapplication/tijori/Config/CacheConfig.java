package com.financialapplication.tijori.Config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Value;

import java.util.concurrent.TimeUnit;

@Configuration
public class CacheConfig {

    public static final String USER_PROFILE_CACHE = "userProfiles";
    public static final String CATEGORY_CACHE = "categoryBudgets";
    public static final String SMS_BY_CATEGORY_CACHE = "transactionsByCategory";
    public static final String EMAIL_OTP_CACHE = "email_otp_cache";
        public static final String REFRESH_TOKEN_CACHE = "refreshTokens";

    @Bean
        public CacheManager cacheManager(@Value("${jwt.refresh-expiration:2592000000}") long refreshExpirationMs) {
        CaffeineCacheManager manager = new CaffeineCacheManager();

        manager.registerCustomCache(USER_PROFILE_CACHE,
                Caffeine.newBuilder()
                        .initialCapacity(100)
                        .maximumSize(500)
                        .expireAfterWrite(10, TimeUnit.MINUTES)
                        .expireAfterAccess(5, TimeUnit.MINUTES)
                        .recordStats()
                        .build());

        manager.registerCustomCache(CATEGORY_CACHE,
                Caffeine.newBuilder()
                        .initialCapacity(100)
                        .maximumSize(500)
                        .expireAfterWrite(10, TimeUnit.MINUTES)
                        .expireAfterAccess(5, TimeUnit.MINUTES)
                        .recordStats()
                        .build());

        manager.registerCustomCache(SMS_BY_CATEGORY_CACHE,
                Caffeine.newBuilder()
                        .initialCapacity(100)
                        .maximumSize(500)
                        .expireAfterWrite(10, TimeUnit.MINUTES)
                        .expireAfterAccess(5, TimeUnit.MINUTES)
                        .recordStats()
                        .build());

        // 5 min TTL for OTPs
        manager.registerCustomCache(EMAIL_OTP_CACHE,
                Caffeine.newBuilder()
                        .initialCapacity(100)
                        .maximumSize(10_000)
                        .expireAfterWrite(5, TimeUnit.MINUTES)
                        .build());

        manager.registerCustomCache(REFRESH_TOKEN_CACHE,
                Caffeine.newBuilder()
                        .initialCapacity(100)
                        .maximumSize(10_000)
                        .expireAfterWrite(refreshExpirationMs, TimeUnit.MILLISECONDS)
                        .recordStats()
                        .build());

        return manager;
    }
}
