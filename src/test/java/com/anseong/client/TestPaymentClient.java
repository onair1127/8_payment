package com.anseong.client;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.anseong.core.domain.client.payment.PaymentPlatform;
import com.anseong.core.domain.client.payment.exception.PaymentFailedException;
import com.anseong.core.domain.client.payment.request.PaymentRequest;
import com.anseong.core.domain.client.payment.request.ScheduleRequest;
import com.anseong.core.domain.client.payment.response.PaymentResponse;
import com.anseong.core.domain.client.payment.response.ScheduleResponse;
import com.anseong.core.infrastructure.client.payment.InternalPaymentClient;

@Component
public class TestPaymentClient implements InternalPaymentClient {

     private static final Logger log = LoggerFactory.getLogger(TestPaymentClient.class);

     @Override
     public PaymentResponse getPayment(PaymentRequest request) {
          log.info("메소드 실행");
          throw new PaymentFailedException("Aborted Test!");
     }

     @Override
     public ScheduleResponse getSchedule(ScheduleRequest request) {
          // TODO Auto-generated method stub
          throw new UnsupportedOperationException("Unimplemented method 'getSchedule'");
     }

     @Override
     public List<ScheduleResponse> getCustomerSchedules(ScheduleRequest request) {
          // TODO Auto-generated method stub
          throw new UnsupportedOperationException("Unimplemented method 'getCustomerSchedules'");
     }

     @Override
     public List<ScheduleResponse> schedule(List<ScheduleRequest> requests) {
          // TODO Auto-generated method stub
          throw new UnsupportedOperationException("Unimplemented method 'schedule'");
     }

     @Override
     public ScheduleResponse unschedule(ScheduleRequest request) {
          // TODO Auto-generated method stub
          throw new UnsupportedOperationException("Unimplemented method 'unschedule'");
     }

     @Override
     public List<ScheduleResponse> unscheduleCustomerAll(ScheduleRequest request) {
          // TODO Auto-generated method stub
          throw new UnsupportedOperationException("Unimplemented method 'unscheduleCustomerAll'");
     }

     @Override
     public PaymentPlatform supports() {
          return PaymentPlatform.TEST;
     }
}
