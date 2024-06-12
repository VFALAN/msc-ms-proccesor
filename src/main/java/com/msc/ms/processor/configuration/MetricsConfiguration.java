package com.msc.ms.processor.configuration;

import io.micrometer.core.aop.CountedAspect;
import io.micrometer.core.aop.TimedAspect;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MetricsConfiguration {

    @Bean
    public TimedAspect timedAspect(final MeterRegistry pMeterRegistry) {
        return new TimedAspect(pMeterRegistry);
    }

    @Bean
    public CountedAspect countedAspect(final MeterRegistry pMeterRegistry) {
        return new CountedAspect(pMeterRegistry);
    }
}
