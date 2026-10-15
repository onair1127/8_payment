package com.anseong.core.infrastructure.client.messanger;

import java.time.Duration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.github.resilience4j.common.retry.configuration.RetryConfigCustomizer;

@Configuration
class MessangerRetryConfig {

     @Bean
     RetryConfigCustomizer messangerRetryConfigCustomizer() {
          return RetryConfigCustomizer.of("messanger", builder -> builder
               .maxAttempts(3)
               .waitDuration(Duration.ofSeconds(3))
               .build()
          );
     }
}
