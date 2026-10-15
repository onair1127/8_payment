package com.anseong.core.infrastructure.client.config.naver;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResourceAccessException;

import com.anseong.core.domain.exception.GlobalAnseongappException;

@Component
class NaverSignatureInterceptor implements ClientHttpRequestInterceptor {

     private final String accessKey;
     private final String secretKey;

     private long timestamp;
     private final String space = " ";
     private final String newLine = "\n";
     private final String method = "POST";

     NaverSignatureInterceptor(
          @Value("${api.naver.access_key}") String accessKey,
          @Value("${api.naver.secret_key}") String secretKey
     ) {
          this.accessKey = new String(Base64.getDecoder().decode(accessKey));
          this.secretKey = new String(Base64.getDecoder().decode(secretKey));
     }

     @Override
     @SuppressWarnings("null")
     public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) {
          try {
               this.timestamp = System.currentTimeMillis();
               request.getHeaders().set("Content-Type", "application/json; UTF-8");
               request.getHeaders().set("x-ncp-apigw-timestamp", "timestamp");
               request.getHeaders().set("x-ncp-iam-access-key", "accessKey");
               request.getHeaders().set("x-ncp-apigw-signature-v2", makeSignature(request.getURI().getRawPath()));
          } catch (UnsupportedEncodingException | NoSuchAlgorithmException | InvalidKeyException e) {
               throw new GlobalAnseongappException("Naver API에서 시그니처 생성 실패 : " + e.getMessage(), e.getCause());
          }

          try {
               return execution.execute(request, body);
          } catch (IOException e) {
               throw new ResourceAccessException("네이버 API 요청 실패 : " + e.getCause() + " : " + e.getMessage());
          }
     }

     private final String makeSignature(String url) throws UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException {
          String message = new StringBuilder()
               .append(method)
               .append(space)
               .append(url)
               .append(newLine)
               .append(timestamp)
               .append(newLine)
               .append(accessKey)
               .toString();

          SecretKeySpec signingKey = new SecretKeySpec(secretKey.getBytes("UTF-8"), "HmacSHA256");
          Mac mac = Mac.getInstance("HmacSHA256");
          mac.init(signingKey);

          byte[] rawHmac = mac.doFinal(message.getBytes("UTF-8"));
          return Base64.getEncoder().encodeToString(rawHmac);
     }
}
