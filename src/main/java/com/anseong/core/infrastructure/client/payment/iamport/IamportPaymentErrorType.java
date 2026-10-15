package com.anseong.core.infrastructure.client.payment.iamport;

import java.util.Arrays;

enum IamportPaymentErrorType {
     GET_PAYMENTS_FAILED      (ResponseBody.Payment.class, "결제내역 단건조회 실패"),
     GET_SCHEDULE_FAILED      (ResponseBody.Schedule.class, "결제예약 단건조회 실패"),
     SCHEDULE_FAILED          (ResponseBody.Schedule.class, "결제예약 실패"),
     UNSCHEDULE_FAILED        (ResponseBody.Schedule.class, "결제예약 취소 실패"),
     UNKNOWN_TARGET_CLASS     (null, "알 수 없는 응답 클래스 타입");

     private final Class<? extends IamportResponseBody> targetClass;
     private final String value;

     IamportPaymentErrorType(Class<? extends IamportResponseBody> targetClass, String value) {
          this.targetClass = targetClass;
          this.value = value;
     }

     String getValue() {
          return value;
     }

     static <T> IamportPaymentErrorType getValueFromClassType(Class<T> clazz) {
          return Arrays.stream(IamportPaymentErrorType.values())
               .filter(e -> e.targetClass == clazz)
               .findFirst()
               .orElse(UNKNOWN_TARGET_CLASS);
     }
}
