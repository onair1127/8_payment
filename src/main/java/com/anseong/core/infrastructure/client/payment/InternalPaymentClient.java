package com.anseong.core.infrastructure.client.payment;

import com.anseong.core.domain.client.payment.PaymentClient;
import com.anseong.core.domain.client.payment.PaymentPlatform;

public interface InternalPaymentClient extends PaymentClient {
     PaymentPlatform supports();
}
