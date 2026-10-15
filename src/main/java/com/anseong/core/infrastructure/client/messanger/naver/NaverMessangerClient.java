package com.anseong.core.infrastructure.client.messanger.naver;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import com.anseong.core.domain.client.messanger.MessangerPlatform;
import com.anseong.core.domain.client.messanger.request.MessangerRequest;
import com.anseong.core.domain.exception.GlobalAnseongappException;
import com.anseong.core.infrastructure.client.messanger.InternalMessangerClient;
import com.anseong.core.infrastructure.client.messanger.naver.ResponseBody.SMS;
import com.anseong.global.utils.JsonUtils;

@Component
class NaverMessangerClient implements InternalMessangerClient {

     private final RestClient naverSignatureRestClient;
     private final StringRedisTemplate redisTemplate;

     NaverMessangerClient(
          @Qualifier("naverSignatureRestClient") RestClient.Builder naverSignatureRestClient,
          StringRedisTemplate redisTemplate
     ) {
          this.naverSignatureRestClient = naverSignatureRestClient
               .baseUrl("https://sens.apigw.ntruss.com/sms/v2/services/ncp:sms:kr:258235742712:anseongapp_receip/messages")
               .build();

          this.redisTemplate = redisTemplate;
     }

     @Override
     public void sendReportSMSNotificationToMe(MessangerRequest request) {
          HashOperations<String, String, String> operations = redisTemplate.opsForHash();
          String content = operations.get("template:message", "REPORT");

          if (content == null)
               content =
               """
                    신고 들어왔는데 템플릿이 비어져있어서 임시 메세지도 대처됨.
                    신고자 아이디 : %s 확인 ㄱㄱ
               """.formatted(request.username());
          else
               content.replace("#{target}", request.username());

          RequestBody.SMS requestBody = RequestBody.SMS.toMe(request, content);
          ResponseBody.SMS response = naverSignatureRestClient.post()
               .body(requestBody)
               .retrieve()
               .onStatus(HttpStatusCode::isError, (req, res) -> throwError(NaverMessangerErrorType.SEND_SMS_FAILED, res, SMS.class))
               .body(ResponseBody.SMS.class);

          if (response == null || !response.isSuccess()) {
               String errorMessage = response != null ? (response.statusCode() + " : " + response.statusName()) : "알 수 없는 에러";

               if (response == null || response.statusCode().startsWith("4"))
                    throw new GlobalAnseongappException(NaverMessangerErrorType.SEND_SMS_FAILED.getValue() + " : " + errorMessage);
               else
                    throw new NaverMessangerFailedException(
                         NaverMessangerErrorType.SEND_SMS_FAILED,
                         HttpStatusCode.valueOf(Integer.parseInt(response.statusCode())),
                         errorMessage
                    );
          }
     }

     @Override
     public MessangerPlatform supports() {
          return MessangerPlatform.NAVER;
     }

     private <T extends BaseNaverNotifiactionResponseBody> void throwError(NaverMessangerErrorType errorType, ClientHttpResponse errorResponse, Class<T> errorResponseType) throws IOException {
          T response = JsonUtils.toObject(errorResponse.getBody(), errorResponseType);

          if (errorResponse.getStatusCode().is4xxClientError()) {
               throw new GlobalAnseongappException(errorType.getValue() + " : " + response.toString());
          } else {
               throw new NaverMessangerFailedException(errorType, errorResponse.getStatusCode(), response.toString());
          }
     }
}
