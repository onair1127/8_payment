package com.anseong.core.domain.model.payment_failed.repository;

import com.anseong.core.domain.model.payment_failed.PaymentFailed;

public interface PaymentFailedCommandRepository {

     void save(PaymentFailed failed);

}
