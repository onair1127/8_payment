package com.anseong.core.application.commands.payment.services;

import com.anseong.core.application.commands.payment.PaymentWebHookCommand;
import com.anseong.core.domain.model.user.UserId;

public interface PaymentService {

	void nextSchedule(PaymentWebHookCommand command);

     void updateCard(PaymentWebHookCommand command);

     void failedSchedule(PaymentWebHookCommand command);

     void cancelSchedule(UserId userId);
}
