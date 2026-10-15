package com.anseong.core.domain.client.payment;

import java.util.List;

import com.anseong.core.domain.client.payment.request.PaymentRequest;
import com.anseong.core.domain.client.payment.request.ScheduleRequest;
import com.anseong.core.domain.client.payment.response.PaymentResponse;
import com.anseong.core.domain.client.payment.response.ScheduleResponse;

public interface PaymentClient {
     PaymentResponse getPayment(PaymentRequest request);

     ScheduleResponse getSchedule(ScheduleRequest request);

     List<ScheduleResponse> getCustomerSchedules(ScheduleRequest request);

     List<ScheduleResponse> schedule(List<ScheduleRequest> requests);

     ScheduleResponse unschedule(ScheduleRequest request);

     List<ScheduleResponse> unscheduleCustomerAll(ScheduleRequest request);
}
