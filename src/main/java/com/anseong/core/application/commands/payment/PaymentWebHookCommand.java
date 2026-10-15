package com.anseong.core.application.commands.payment;

import com.anseong.core.domain.client.payment.PaymentPlatform;

public record PaymentWebHookCommand(
     PaymentPlatform platform
     , String uid
     , String merchantUid
     , String status
) {}
