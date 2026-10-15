package com.anseong.core.domain.client.payment.exception;

import com.anseong.core.domain.exception.GlobalAnseongappException;

public class PaymentFailedException extends GlobalAnseongappException {

     private static final long serialVersionUID = -5485022518917514314L;

     public PaymentFailedException(String message) {
          super(message);
     }

     public PaymentFailedException(String message, Throwable cause) {
          super(message, cause);
     }
}
