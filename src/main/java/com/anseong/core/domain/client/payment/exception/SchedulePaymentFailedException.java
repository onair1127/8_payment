package com.anseong.core.domain.client.payment.exception;

import com.anseong.core.domain.exception.GlobalAnseongappException;
import com.anseong.core.domain.model.user.UserId;

public class SchedulePaymentFailedException extends GlobalAnseongappException {
     private static final long serialVersionUID = -7961334806823005019L;

     public SchedulePaymentFailedException(UserId userId, String message) {
          super(String.format("[%d] 결제 예약 실패 : %s", userId.value(), message));
     }

     public SchedulePaymentFailedException(UserId userId, String message, Throwable cause) {
          super(String.format("[%d] 결제 예약 실패 : %s", userId.value(), message), cause);
     }
}
