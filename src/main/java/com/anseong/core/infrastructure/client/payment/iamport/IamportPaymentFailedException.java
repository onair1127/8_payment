package com.anseong.core.infrastructure.client.payment.iamport;

import org.springframework.http.HttpStatusCode;

import com.anseong.core.domain.client.payment.exception.PaymentFailedException;
import com.anseong.core.domain.model.user.UserId;

class IamportPaymentFailedException extends PaymentFailedException {

     private static final long serialVersionUID = -115898769617964715L;

     public IamportPaymentFailedException(UserId userId, IamportPaymentErrorType errorType, HttpStatusCode statusCode, String message) {
          super(String.format("[%d] 아임포트 | %s : %d - %s",
               userId.value(),
               errorType.getValue(),
               statusCode.value(),
               message
          ));
     }

     public IamportPaymentFailedException(UserId userId, IamportPaymentErrorType errorType, HttpStatusCode statusCode, String message, Throwable cause) {
          super(String.format("[%d] 아임포트 | %s : %d - %s",
               userId.value(),
               errorType.getValue(),
               statusCode.value(),
               message
          ), cause);
     }
}
