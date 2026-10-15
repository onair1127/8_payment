package com.anseong.core.infrastructure.client.config.naver;

import java.io.IOException;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResourceAccessException;

@Component
class NaverStaticInterceptor implements ClientHttpRequestInterceptor {

     private final String clientId;
     private final String clientSecret;

     NaverStaticInterceptor(
          @Value("${api.naver.client_id}") String clientId,
          @Value("${api.naver.client_secret}") String clientSecret
     ) {
          this.clientId = new String(Base64.getDecoder().decode(clientId));
          this.clientSecret = new String(Base64.getDecoder().decode(clientSecret));
     }

     @Override
     @SuppressWarnings("null")
     public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) {
          request.getHeaders().set("X-NCP-APIGW-API-KEY-ID", clientId);
          request.getHeaders().set("X-NCP-APIGW-API-KEY", clientSecret);
          request.getHeaders().set("Accept", "application/json");

          request.getURI().getRawPath();

          try {
               return execution.execute(request, body);
          } catch (IOException e) {
               throw new ResourceAccessException("네이버 API 요청 실패 : " + e.getCause() + " : " + e.getMessage());
          }
     }
}
