package com.anseong.core.infrastructure.client.messanger.naver;

import org.springframework.http.HttpStatusCode;

import com.anseong.core.domain.client.messanger.exception.MessangerSendFailedException;

class NaverMessangerFailedException extends MessangerSendFailedException {

     private static final long serialVersionUID = 3329532565853995158L;

     public NaverMessangerFailedException(NaverMessangerErrorType errorType, HttpStatusCode statusCode, String message) {
          super(String.format("%s : %d - %s",
               errorType.getValue(),
               statusCode.value(),
               message
          ));
     }

     public NaverMessangerFailedException(NaverMessangerErrorType errorType, HttpStatusCode statusCode, String message, Throwable cause) {
          super(String.format("%s : %d - %s",
               errorType.getValue(),
               statusCode.value(),
               message
          ), cause);
     }
}
