package com.financialapplication.tijori.Config;

import org.springframework.boot.actuate.info.Info;
import org.springframework.boot.actuate.info.InfoContributor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;


@Component
public class ApplicationInfoContributor implements InfoContributor {

    private final LocalDateTime startupTime = LocalDateTime.now();

    @Override
    public void contribute(Info.Builder builder) {
        Map<String, Object> appDetails = new HashMap<>();
        appDetails.put("name", "Tijori - Expense Tracker API");
        appDetails.put("description", "RESTful API for personal finance management and expense tracking with SMS-based transaction analysis");
        appDetails.put("version", "1.0.0");
        appDetails.put("java-version", System.getProperty("java.version"));
        appDetails.put("spring-boot-version", org.springframework.boot.SpringBootVersion.getVersion());

        Map<String, Object> features = new HashMap<>();
        features.put("authentication", "JWT-based stateless authentication");
        features.put("rate-limiting", "Token bucket algorithm with Bucket4j");
        features.put("caching", "Caffeine high-performance local cache");
        features.put("metrics", "Micrometer with Prometheus export");
        features.put("tracing", "Correlation ID based request tracking");

        builder.withDetail("application", appDetails);
        builder.withDetail("features", features);
        builder.withDetail("startup-time", startupTime.toString());
    }
}

