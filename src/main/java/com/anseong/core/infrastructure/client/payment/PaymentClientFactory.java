package com.anseong.core.infrastructure.client.payment;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import com.anseong.core.domain.aop.fallback.FallbackToQueue;
import com.anseong.core.domain.client.payment.PaymentClient;
import com.anseong.core.domain.client.payment.PaymentPlatform;
import com.anseong.core.domain.client.payment.request.PaymentRequest;
import com.anseong.core.domain.client.payment.request.ScheduleRequest;
import com.anseong.core.domain.client.payment.response.PaymentResponse;
import com.anseong.core.domain.client.payment.response.ScheduleResponse;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;

@Component
@Primary
@Retry(name = "payment")
@CircuitBreaker(name = "payment")
@FallbackToQueue(queueName = "payment")
class PaymentClientFactory implements PaymentClient {

     private final Map<PaymentPlatform, InternalPaymentClient> clients;
     private final PaymentPlatform DEFAULT_PLATFORM = PaymentPlatform.IAMPORT;

     PaymentClientFactory(List<InternalPaymentClient> paymentClients) {
          this.clients = paymentClients.stream()
               .collect(Collectors.toUnmodifiableMap(
                    InternalPaymentClient::supports,
                    Function.identity()
               ));
     }

     private InternalPaymentClient getClient(PaymentPlatform platform) {
          if (platform == null)
               platform = DEFAULT_PLATFORM;

          return clients.get(platform);
     }

     @Override
     public PaymentResponse getPayment(PaymentRequest request) {
          return getClient(request.platform()).getPayment(request);
     }

     @Override
     public ScheduleResponse getSchedule(ScheduleRequest request) {
          return getClient(request.platform()).getSchedule(request);
     }

     @Override
     public List<ScheduleResponse> getCustomerSchedules(ScheduleRequest request) {
          return getClient(request.platform()).getCustomerSchedules(request);
     }

     @Override
     public List<ScheduleResponse> schedule(List<ScheduleRequest> requests) {
          if (requests.isEmpty())
               return List.of();

          return getClient(requests.get(0).platform()).schedule(requests);
     }

     @Override
     public ScheduleResponse unschedule(ScheduleRequest request) {
          return getClient(request.platform()).unschedule(request);
     }

     @Override
     public List<ScheduleResponse> unscheduleCustomerAll(ScheduleRequest request) {
          return getClient(request.platform()).unscheduleCustomerAll(request);
     }
}
