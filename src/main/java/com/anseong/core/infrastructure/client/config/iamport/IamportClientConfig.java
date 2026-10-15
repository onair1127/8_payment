package com.anseong.core.infrastructure.client.config.iamport;

import java.time.Duration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

@Configuration
class IamportClientConfig {

     @Bean
     RestClient iamportRestClient(IamportAuthorizationInterceptor iamportAuthorizationInterceptor) {
          SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
          factory.setConnectTimeout(Duration.ofMinutes(1));
          factory.setReadTimeout(Duration.ofMillis(1));

          return RestClient.builder()
               .baseUrl("https://api.iamport.kr")
               .requestFactory(factory)
               .requestInterceptor(iamportAuthorizationInterceptor)
               .defaultHeaders(headers -> headers.setContentType(MediaType.APPLICATION_JSON))
               .build();
     }
}
