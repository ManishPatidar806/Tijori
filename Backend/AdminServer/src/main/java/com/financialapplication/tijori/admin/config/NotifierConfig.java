package com.financialapplication.tijori.admin.config;

import de.codecentric.boot.admin.server.domain.entities.InstanceRepository;
import de.codecentric.boot.admin.server.notify.CompositeNotifier;
import de.codecentric.boot.admin.server.notify.LoggingNotifier;
import de.codecentric.boot.admin.server.notify.Notifier;
import de.codecentric.boot.admin.server.notify.filter.FilteringNotifier;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.Collections;
import java.util.List;

@Configuration
public class NotifierConfig {

    private final InstanceRepository instanceRepository;

    public NotifierConfig(InstanceRepository instanceRepository) {
        this.instanceRepository = instanceRepository;
    }

    @Bean
    @Primary
    public CompositeNotifier compositeNotifier(ObjectProvider<List<Notifier>> notifiers) {
        return new CompositeNotifier(notifiers.getIfAvailable(Collections::emptyList));
    }

    @Bean
    public LoggingNotifier loggingNotifier() {
        return new LoggingNotifier(instanceRepository);
    }

    @Bean
    public FilteringNotifier filteringNotifier(LoggingNotifier loggingNotifier) {
        // Add custom filtering rules here if needed
        return new FilteringNotifier(loggingNotifier, instanceRepository);
    }

}


