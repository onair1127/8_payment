package com.anseong.core.infrastructure.client.config.naver;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
class NaverClientConfig {

     @Bean
     RestClient.Builder naverStaticRestClient(NaverStaticInterceptor interceptor) {
          return RestClient.builder()
               .requestInterceptor(interceptor);
     }

     @Bean
     RestClient.Builder naverSignatureRestClient(NaverSignatureInterceptor interceptor) {
          return RestClient.builder()
               .requestInterceptor(interceptor);
     }
}
