package com.anseong.core.application.commands.payment.services;

import com.anseong.core.domain.client.payment.response.PaymentResponse;
import com.anseong.core.domain.model.user.UserId;

public interface PaymentRepositoryService {
     void updatePaymentUid(UserId userId, String merchantUid, String customerUid);

     void saveFailedPayment(UserId userId, PaymentResponse failedPaymentResponse);
}
