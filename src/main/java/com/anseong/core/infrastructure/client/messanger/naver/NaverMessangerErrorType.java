package com.anseong.core.infrastructure.client.messanger.naver;

enum NaverMessangerErrorType {
     SEND_SMS_FAILED("SMS 전송에 실패하였습니다.");

     private final String value;

     NaverMessangerErrorType(String value) {
          this.value = value;
     }

     String getValue() {
          return value;
     }
}
