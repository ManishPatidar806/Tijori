package com.financialapplication.tijori.Config;

import com.financialapplication.tijori.Exception.RateLimitExceededException;
import io.github.bucket4j.Bucket;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;


@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class RateLimitAspect {

    private final RateLimitConfig rateLimitConfig;

    @Around("@annotation(rateLimited)")
    public Object enforceRateLimit(ProceedingJoinPoint joinPoint, RateLimited rateLimited) throws Throwable {
        String clientIp = getClientIp();
        String key = clientIp + ":" + rateLimited.bucketType().name();
        
        Bucket bucket = rateLimitConfig.resolveBucket(key, rateLimited.bucketType());
        
        if (bucket.tryConsume(1)) {
            log.debug("Rate limit passed for client: {} on method: {}", clientIp, joinPoint.getSignature().getName());
            return joinPoint.proceed();
        } else {
            log.warn("Rate limit exceeded for client: {} on method: {}", clientIp, joinPoint.getSignature().getName());
            throw new RateLimitExceededException("Too many requests. Please try again later.");
        }
    }

    private String getClientIp() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            return "unknown";
        }
        HttpServletRequest request = attributes.getRequest();
        return request.getRemoteAddr();
    }
}
