package com.anseong.core.infrastructure.client.config.iamport;

import org.springframework.http.HttpStatusCode;

import com.anseong.core.domain.client.common.exception.AuthorizationFailedException;

class IamportAuthorizationFailedException extends AuthorizationFailedException {

     private static final long serialVersionUID = 7223623633055324536L;

     public IamportAuthorizationFailedException(HttpStatusCode statusCode, String message) {
          super(String.format("아임포트 API 인증 토큰 발급 실패 : %d - %s", statusCode.value(), message));
     }

     public IamportAuthorizationFailedException(HttpStatusCode statusCode, String message, Throwable cause) {
          super(String.format("아임포트 API 인증 토큰 발급 실패 : %d - %s", statusCode.value(), message), cause);
     }
}
