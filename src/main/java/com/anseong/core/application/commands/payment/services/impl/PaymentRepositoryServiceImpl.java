package com.anseong.core.application.commands.payment.services.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.anseong.core.application.commands.payment.services.PaymentRepositoryService;
import com.anseong.core.domain.client.payment.response.PaymentResponse;
import com.anseong.core.domain.model.payment_failed.PaymentFailed;
import com.anseong.core.domain.model.payment_failed.repository.PaymentFailedCommandRepository;
import com.anseong.core.domain.model.user.UserId;
import com.anseong.core.domain.model.user.repository.UserCommandRepository;

@Service
@Transactional(isolation = Isolation.READ_COMMITTED)
class PaymentRepositoryServiceImpl implements PaymentRepositoryService {

     private final PaymentFailedCommandRepository paymentFailedCommandRepository;
     private final UserCommandRepository userCommandRepository;

     public PaymentRepositoryServiceImpl(
          PaymentFailedCommandRepository paymentFailedCommandRepository,
          UserCommandRepository userCommandRepository
     ) {
          this.paymentFailedCommandRepository = paymentFailedCommandRepository;
          this.userCommandRepository = userCommandRepository;
     }

     @Override
     public void updatePaymentUid(UserId userId, String merchantUid, String customerUid) {
          userCommandRepository.updatePaymentUid(userId, customerUid, merchantUid);
     }

     @Override
     public void saveFailedPayment(UserId userId, PaymentResponse failedPaymentResponse) {
          PaymentFailed paymentFailed = PaymentFailed.create(userId, failedPaymentResponse);

          paymentFailedCommandRepository.save(paymentFailed);
          userCommandRepository.updatePaymentUid(userId, failedPaymentResponse.customerUid(), failedPaymentResponse.merchantUid());
     }
}
