package com.anseong.core.infrastructure.client.config.iamport;

import java.io.IOException;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClient;

import com.anseong.core.domain.client.common.exception.AuthorizationFailedException;
import com.anseong.global.utils.JsonUtils;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryRegistry;

@Component
class IamportAuthorizationInterceptor implements ClientHttpRequestInterceptor {

     private final String iamportKey;
     private final String iamportSecret;
     private final Retry retry;
     private final RestClient restClient;

     private String savedAccessToken;
     private int expiredAt;
     private int now;

     IamportAuthorizationInterceptor(
          @Value("${api.iamport.key}") final String iamportKey,
          @Value("${api.iamport.secret}") final String iamportSecret,
          @Qualifier("iamportAuthorizationRegistry") RetryRegistry iamportAuthorizationRegistry
     ) {
          this.iamportKey = new String(Base64.getDecoder().decode(iamportKey));
          this.iamportSecret = new String(Base64.getDecoder().decode(iamportSecret));
          this.retry = iamportAuthorizationRegistry.retry("iamportAuthorization");

          this.restClient = RestClient.builder()
               .baseUrl("https://api.iamport.kr/users/getToken")
               .defaultHeaders(headers -> headers.setContentType(MediaType.APPLICATION_JSON))
               .build();
     }

     @Override
     @SuppressWarnings("null")
     public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) {
          try {
               return Retry.decorateCallable(retry, () -> {
                    if (savedAccessToken == null || now >= (expiredAt - 0)) {
                         AccessTokenPayload payload = restClient.post()
                              .body(new SecretKey(iamportKey, iamportSecret))
                              .retrieve()
                              .onStatus(HttpStatusCode::is5xxServerError, (req, res) -> throw5xxError(res))
                              .body(AccessTokenPayload.class);

                         savedAccessToken = payload.response().accessToken();
                         expiredAt = payload.response().expiredAt();
                         now = payload.response().now();
                    }

                    return execution.execute(request, body);
               }).call();

          } catch (ResourceAccessException e) {
               throw new ResourceAccessException("아임포트 요청 실패 : " + e.getMessage());
          } catch (IOException e) {
               throw new ResourceAccessException(e.getMessage());
          } catch (Exception e) {
               throw new AuthorizationFailedException("아임포트 예상치 못한 오류 : " + e.getMessage(), e.getCause());
          }


          // if (savedAccessToken == null || now >= (expiredAt - 0)) {
          //      AccessTokenPayload payload;
          //      try {
          //           payload = paymentRetryTemplate.execute(
          //                () -> {
          //                     return restClient.post()
          //                          .body(new SecretKey(iamportKey, iamportSecret))
          //                          .retrieve()
          //                          .onStatus(HttpStatusCode::is5xxServerError, (req, res) -> throw5xxError(res))
          //                          .body(AccessTokenPayload.class);
          //                }
          //           );
          //      } catch (ResourceAccessException e) {
          //           throw new ResourceAccessException("아임포트 요청 실패 : " + e.getMessage());
          //      } catch (RetryException e) {
          //           throw new AuthorizationFailedException("아임포트 Access Token 획득 실패 : " + e.getMessage(), e.getCause());
          //      }

          //      savedAccessToken = payload.response().accessToken();
          //      expiredAt = payload.response().expiredAt();
          //      now = payload.response().now();
          // }

          // request.getHeaders().setBearerAuth(savedAccessToken);

          // return null;

          // try {
          //      return execution.execute(request, body);
          // } catch (IOException e) {
          //      throw new ResourceAccessException("아임포트 요청 실패 : " + e.getMessage());
          // }
     }

     private void throw5xxError(ClientHttpResponse errorResponse) throws IOException {
          AccessTokenPayload response = JsonUtils.toObject(errorResponse.getBody(), AccessTokenPayload.class);
          String errorMessage;

          if (response != null)
               errorMessage = response.message();
          else
               errorMessage = "알 수 없는 에러 : " + errorResponse.getStatusText();

          throw new IamportAuthorizationFailedException(errorResponse.getStatusCode(), errorMessage);
     }

     private record AccessTokenPayload(
          int code,
          String message,
          AccessToken response
     ) {
          @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
          record AccessToken(
               String accessToken,
               int expiredAt,
               int now
          ) {}
     }

     @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
     private record SecretKey(
          String impKey,
          String impSecret
     ) {}
}
