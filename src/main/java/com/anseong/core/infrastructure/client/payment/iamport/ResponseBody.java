package com.anseong.core.infrastructure.client.payment.iamport;

import java.util.List;

import org.springframework.http.HttpStatus;

import com.anseong.core.domain.client.payment.response.PaymentResponse;
import com.anseong.core.domain.client.payment.response.ScheduleResponse;
import com.anseong.core.domain.model.user.UserId;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

record IamportPayload<T extends IamportResponseBody>(
     int code,
     String message,
     T response
) {
     T response(UserId userId) {
          if (code != 0) {
               throw new IamportPaymentFailedException(userId, IamportPaymentErrorType.getValueFromClassType(response.getClass()), HttpStatus.OK, message);
          }

          return response;
     }

     /**
      * <h2>해당 메소드 사용 금지.</h2>
      * 사용자 추적이 필수이므로 response(userId) 사용 요망.
      */
     @Deprecated
     public T response() {
          throw new UnsupportedOperationException("해당 메소드 사용 금지. 사용자 추적이 필수이므로 response(userId) 사용 요망.");
     }
}

record IamportMultipleResponsePayload<T extends IamportResponseBody>(
     int code,
     String message,
     List<T> response,

     @JsonIgnore
     Class<T> targetClass
) {
     List<T> response(UserId userId) {
          if (code != 0) {
               throw new IamportPaymentFailedException(userId, IamportPaymentErrorType.getValueFromClassType(response.getClass()), HttpStatus.OK, message);
          }

          return response;
     }

     /**
      * <h2>해당 메소드 사용 금지.</h2>
      * 사용자 추적이 필수이므로 response(userId) 사용 요망.
      */
     @Deprecated
     public List<T> response() {
          throw new UnsupportedOperationException("해당 메소드 사용 금지. 사용자 추적이 필수이므로 response(userId) 사용 요망.");
     }
}

record IamportErrorResponse(
     int code,
     String message
) {}

final class ResponseBody {
     private ResponseBody() {}

     @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
     record Payment (
          String impUid,
          String customerUid,
          String merchantUid,
          String name,
          long amount,
          String buyerName,
          String buyerEmail,
          String buyerTel,
          String buyerAddr,
          String customData,
          String failReason,
          String cancelReason,
          String status,
          String receiptUrl,
          boolean cashReceiptIssued
     ) implements BaseIamportResponseBody<PaymentResponse> {

          @Override
          public PaymentResponse toDomainResponse(UserId userId) {
               return PaymentResponse.builder()
                    .userId(userId)
                    .uid(impUid)
                    .customerUid(customerUid)
                    .merchantUid(merchantUid)
                    .status(status)
                    .name(name)
                    .amount(amount)
                    .buyerName(buyerName)
                    .buyerEmail(buyerEmail)
                    .buyerTel(buyerTel)
                    .buyerAddr(buyerAddr)
                    .failReason(failReason)
                    .cancelReason(cancelReason)
                    .receiptUrl(receiptUrl)
                    .cashReceiptIssued(cashReceiptIssued)
                    .build();
          }
     }

     @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
     record Schedule(
          String impUid,
          String customerUid,
          String merchantUid,
          String name,
          long amount,
          String buyerName,
          String buyerEmail,
          String buyerTel,
          String buyerAddr,
          String customData,
          String failReason,
          long scheduleAt,
          String scheduleStatus
     ) implements BaseIamportResponseBody<ScheduleResponse> {

          @Override
          public ScheduleResponse toDomainResponse(UserId userId) {
               return ScheduleResponse.builder()
                    .userId(userId)
                    .customerUid(customerUid)
                    .merchantUid(merchantUid)
                    .buyerName(buyerName)
                    .buyerEmail(buyerEmail)
                    .buyerTel(buyerTel)
                    .buyerAddr(buyerAddr)
                    .failReason(failReason)
                    .scheduleStatus(scheduleStatus)
                    .scheduleAt(scheduleAt)
                    .build();
          }
     }
}
