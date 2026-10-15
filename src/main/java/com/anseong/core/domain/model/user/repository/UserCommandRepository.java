package com.anseong.core.domain.model.user.repository;

import com.anseong.core.domain.client.payment.response.PaymentResponse;
import com.anseong.core.domain.model.user.UserId;

public interface UserCommandRepository {
     void updatePaymentUid(UserId userId, String merchantUid, String customerUid);

     void saveFailedPayment(UserId userId, PaymentResponse failedPaymentResponse);
}
