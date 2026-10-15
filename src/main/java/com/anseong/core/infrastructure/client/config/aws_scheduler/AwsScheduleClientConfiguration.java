package com.anseong.core.infrastructure.client.config.aws_scheduler;

import java.time.Duration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.http.nio.netty.NettyNioAsyncHttpClient;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.scheduler.SchedulerAsyncClient;
import software.amazon.awssdk.services.scheduler.SchedulerClient;

@Configuration
class AwsScheduleClientConfiguration {

     @Bean
     SchedulerClient awsSchedulerClient() {
          return SchedulerClient.builder()
               .credentialsProvider(DefaultCredentialsProvider.create())
               .region(Region.AP_NORTHEAST_2)
               .build();
     }

     @Bean
     SchedulerAsyncClient awsSchedulerAsyncClient() {
          return SchedulerAsyncClient.builder()
               .credentialsProvider(DefaultCredentialsProvider.create())
               .region(Region.AP_NORTHEAST_2)
               .httpClient(NettyNioAsyncHttpClient.builder()
                    .maxConcurrency(500)
                    .maxPendingConnectionAcquires(50_000)
                    .connectionAcquisitionTimeout(Duration.ofSeconds(30))
                    .build())
               .build();
     }
}
