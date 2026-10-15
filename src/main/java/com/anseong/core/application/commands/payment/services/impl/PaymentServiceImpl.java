package com.anseong.core.application.commands.payment.services.impl;

import com.anseong.core.application.commands.payment.PaymentWebHookCommand;
import com.anseong.core.application.commands.payment.services.PaymentRepositoryService;
import com.anseong.core.application.commands.payment.services.PaymentService;
import com.anseong.core.application.queries.user.services.UserQueryRepositoryService;
import com.anseong.core.domain.client.messanger.MessangerClient;
import com.anseong.core.domain.client.messanger.MessangerTemplate;
import com.anseong.core.domain.client.messanger.MessangerType;
import com.anseong.core.domain.client.messanger.request.MessangerRequest;
import com.anseong.core.domain.client.payment.PaymentClient;
import com.anseong.core.domain.client.payment.PaymentPlatform;
import com.anseong.core.domain.client.payment.request.PaymentRequest;
import com.anseong.core.domain.client.payment.request.ScheduleRequest;
import com.anseong.core.domain.client.payment.response.PaymentResponse;
import com.anseong.core.domain.client.payment.response.ScheduleResponse;
import com.anseong.core.domain.model.payment_failed.repository.PaymentFailedCommandRepository;
import com.anseong.core.domain.model.user.UserId;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

@Service
public class PaymentServiceImpl implements PaymentService {

     private final ZoneId KST = ZoneId.of("Asia/Seoul");
     private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss");

     private final PaymentClient paymentClient;
     private final PaymentRepositoryService paymentRepositoryService;
     private final PaymentFailedCommandRepository paymentFailedCommandRepository;
     private final UserQueryRepositoryService userQueryRepositoryService;
     private final MessangerClient messangerClient;

     public PaymentServiceImpl(
               PaymentClient paymentClient
               , PaymentRepositoryService paymentRepositoryService
               , PaymentFailedCommandRepository paymentFailedCommandRepository
               , UserQueryRepositoryService userQueryRepositoryService
               , MessangerClient messangerClient
     ) {
          this.paymentClient = paymentClient;
          this.paymentRepositoryService = paymentRepositoryService;
          this.paymentFailedCommandRepository = paymentFailedCommandRepository;
          this.userQueryRepositoryService = userQueryRepositoryService;
          this.messangerClient = messangerClient;
     }

     @Override
     public void nextSchedule(PaymentWebHookCommand command) {
          Assert.notNull(command.uid(), "결제 서비스 : 파라미터 `command`가 널입니다.");

          /**
           * 1. 결제 정보 불러오기
           */
          PaymentResponse getPaymentResponse = getPayment(command);

          /**
           * 2. 다음 결제 스케쥴링
           */
          final UserId userId = new UserId(Long.parseLong(command.merchantUid().split("-")[1]));
          List<ScheduleResponse> scheduleResponses = schedule(userId, command.platform(), getPaymentResponse);
          ScheduleResponse lastScheduleResponse = scheduleResponses.get(scheduleResponses.size()-1);

          /**
           * 3. 예약 완료 후 사용자 DB의 고객번호와 주문번호 업데이트
           */
          paymentRepositoryService.updatePaymentUid(userId, lastScheduleResponse.customerUid(), lastScheduleResponse.merchantUid());
     }

     @Override
     public void updateCard(PaymentWebHookCommand command) {
          Assert.notNull(command.uid(), "카드 변경 : 파라미터 `command`가 널입니다.");

          /**
           * 1. 새로운 예약을 위한 데이터 준비
           */
          final UserId userId = new UserId(Long.parseLong(command.merchantUid().split("-")[1]));
          final String customerUid = userQueryRepositoryService.findCustomerUidById(userId);

          /**
           * 2. 카드 변경후 카드 정보 로드
           */
          PaymentResponse updatedPaymentResponse = getPayment(command);

          /**
           * 3. 커두 변경후 카드 정보를 토대로 결제 취소 및 재예약
           */
          List<ScheduleResponse> unscheduleCustomerResponses = unscheduleCustomerAll(userId, customerUid);
          List<ScheduleResponse> updatedScheduleResponses = reschedule(userId, command.platform(), updatedPaymentResponse, unscheduleCustomerResponses);
          ScheduleResponse lastScheduleResponse = updatedScheduleResponses.get(updatedScheduleResponses.size()-1);

          paymentRepositoryService.updatePaymentUid(userId, lastScheduleResponse.customerUid(), lastScheduleResponse.merchantUid());
     }

     @Override
     @Transactional(isolation = Isolation.READ_COMMITTED)
     public void failedSchedule(PaymentWebHookCommand command) {
          Assert.notNull(command.uid(), "결제 상태 반영 : 파라미터 `command`가 널입니다.");

          PaymentResponse failedPaymentResponse = getPayment(command);

          final UserId userId = new UserId(Long.parseLong(command.merchantUid().split("-")[1]));
          final String customerUid = userQueryRepositoryService.findCustomerUidById(userId);

          List<ScheduleResponse> cancalableResponses = unscheduleCustomerAll(userId, customerUid);
          reschedule(userId, command.platform(), failedPaymentResponse, cancalableResponses);

          paymentRepositoryService.saveFailedPayment(userId, failedPaymentResponse);

          // TODO.
          // notificationManagemant.sendSMSToPhone(failed.getBuyerName(), failed.getBuyerTel(), failed.getFailedReason(), failed.getCancelReason());
     }

     @Override
     public void cancelSchedule(UserId userId) {
          String customerUid = userQueryRepositoryService.findCustomerUidById(userId);

          unscheduleCustomerAll(userId, customerUid);
     }

     private PaymentResponse getPayment(PaymentWebHookCommand command) {
          PaymentRequest paymentRequest = PaymentRequest.builder()
               .uid(command.uid())
               .merchantUid(command.merchantUid())
               .build();

          return paymentClient.getPayment(paymentRequest);
     }

     private List<ScheduleResponse> schedule(UserId userId, PaymentPlatform platform, PaymentResponse paymentResponse) {
          ZonedDateTime now = LocalDateTime.now().atZone(KST);
          String newMerchantUid = "SCH-" + userId.value() + "-" + now.format(formatter);
          ScheduleRequest nextScheduleRequest = ScheduleRequest.of(
               platform,
               paymentResponse,
               newMerchantUid,
               now.plusMonths(1)
          );

          List<ScheduleRequest> scheduleRequests = List.of(nextScheduleRequest);

          /**
           * 사용자 문제로 실패했을 경우 1일뒤에 다시 결제되도록 예약
           */
          if (paymentResponse.failReason() != null) {
               scheduleRequests.add(nextScheduleRequest.withMerchantUidAndScheduleAt(
                    "RETRY:" + newMerchantUid,
                    now.plusDays(1)
               ));

               MessangerRequest request = MessangerRequest.builder()
                    .type(MessangerType.LMS)
                    .template(MessangerTemplate.USER_PAYMENT_FAILED)
                    .username(paymentResponse.buyerName())
                    .email(paymentResponse.buyerEmail())
                    .phone(paymentResponse.buyerTel())
                    .build();

               messangerClient.sendToUserWhenPaymentFailed(request);
          }

          return paymentClient.schedule(scheduleRequests);
     }

     private List<ScheduleResponse> reschedule(UserId userId, PaymentPlatform platform, PaymentResponse paymentResponse, List<ScheduleResponse> unscheduledResponses) {
          final ZoneId KST = ZoneId.of("Asia/Seoul");
          final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss");

          List<ScheduleRequest> updatedScheduleResponse = new ArrayList<>();

          for (ScheduleResponse unscheduleResponse : unscheduledResponses) {
               ZonedDateTime scheduleAt = Instant.ofEpochMilli(unscheduleResponse.scheduleAt()).atZone(KST);
               String updatedScheduleMerchantUid = "SCH-" + userId.value() + "-" + scheduleAt.format(formatter);

               updatedScheduleResponse.add(ScheduleRequest.of(
                    platform,
                    paymentResponse,
                    updatedScheduleMerchantUid,
                    scheduleAt
               ));
          }
          return paymentClient.schedule(updatedScheduleResponse);
     }

     private List<ScheduleResponse> unscheduleCustomerAll(UserId userId, String customerUid) {
          ScheduleRequest unscheduleCustomerRequest = ScheduleRequest.builder()
               .customerUid(customerUid)
               .build();

          return paymentClient.unscheduleCustomerAll(unscheduleCustomerRequest);
     }
}
