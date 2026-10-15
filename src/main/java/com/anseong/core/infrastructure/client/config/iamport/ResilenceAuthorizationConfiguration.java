package com.anseong.core.infrastructure.client.config.iamport;

import java.time.Duration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.github.resilience4j.retry.RetryConfig;
import io.github.resilience4j.retry.RetryRegistry;

@Configuration
public class ResilenceAuthorizationConfiguration {

     @Bean
     RetryRegistry iamportAuthorizationRegistry() {
          RetryConfig config = RetryConfig.custom()
               .maxAttempts(3)
               .waitDuration(Duration.ofSeconds(5))
               .build();

          return RetryRegistry.of(config);
     }
}
