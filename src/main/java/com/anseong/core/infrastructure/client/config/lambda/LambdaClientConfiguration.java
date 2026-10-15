package com.anseong.core.infrastructure.client.config.lambda;

import java.time.Duration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.http.nio.netty.NettyNioAsyncHttpClient;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.lambda.LambdaAsyncClient;
import software.amazon.awssdk.services.lambda.LambdaClient;

@Configuration
class LambdaClientConfiguration {

     @Bean
     LambdaClient lambdaClient() {
          return LambdaClient.builder()
               .credentialsProvider(DefaultCredentialsProvider.create())
               .region(Region.AP_NORTHEAST_2)
               .build();
     }

     @Bean
     LambdaAsyncClient lambdaAsyncClient() {
          return LambdaAsyncClient.builder()
               .credentialsProvider(DefaultCredentialsProvider.create())
               .region(Region.AP_NORTHEAST_2)
               .httpClient(NettyNioAsyncHttpClient.builder()
                    .maxConcurrency(200)
                    .maxPendingConnectionAcquires(100)
                    .connectionAcquisitionTimeout(Duration.ofMinutes(1))
                    .build())
               .build();
     }
}
