package com.financialapplication.tijori.Config;

import io.micrometer.core.aop.TimedAspect;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MetricsConfig {

    private final MeterRegistry meterRegistry;

    public MetricsConfig(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
    }

    @Bean
    public TimedAspect timedAspect() {
        return new TimedAspect(meterRegistry);
    }

    @Bean
    public Counter userRegistrationCounter() {
        return Counter.builder("app.users.registrations")
                .description("Total number of user registrations")
                .register(meterRegistry);
    }

    @Bean
    public Counter userLoginCounter() {
        return Counter.builder("app.users.logins")
                .description("Total number of user logins")
                .register(meterRegistry);
    }

    @Bean
    public Counter transactionCounter() {
        return Counter.builder("app.transactions.saved")
                .description("Total number of transactions saved")
                .register(meterRegistry);
    }

    @Bean
    public Counter categorizationCounter() {
        return Counter.builder("app.expenses.categorized")
                .description("Total number of expenses categorized")
                .register(meterRegistry);
    }
}

