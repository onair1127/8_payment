package com.anseong.core.web.apis;

import jakarta.validation.Valid;

import com.anseong.core.application.commands.payment.PaymentWebHookCommand;
import com.anseong.core.application.commands.payment.services.PaymentService;
import com.anseong.core.domain.client.payment.PaymentClient;
import com.anseong.core.domain.client.payment.request.PaymentRequest;
import com.anseong.core.domain.client.payment.request.ScheduleRequest;
import com.anseong.core.domain.client.payment.response.ScheduleResponse;
import com.anseong.core.domain.client.schedule.ScheduleClient;
import com.anseong.core.domain.exception.GlobalAnseongappException;
import com.anseong.core.web.payloads.IamportWebHookPayload;
import com.anseong.core.web.result.ApiResult;
import com.anseong.core.web.result.Result;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
class PaymentController {

     private PaymentService paymentService;
     private PaymentClient paymentClient;
     private ScheduleClient scheduleClient;

     PaymentController(PaymentService paymentService, PaymentClient paymentClient, ScheduleClient scheduleClient) {
          this.paymentService = paymentService;
          this.paymentClient = paymentClient;
          this.scheduleClient = scheduleClient;
     }

     @PostMapping("/api/iamport/notification")
     ResponseEntity<ApiResult> webHook(
          @Valid @RequestBody IamportWebHookPayload payload
     ) {
          PaymentWebHookCommand command = payload.toCommand();

          if (command.status().equals("paid")) {
               if (command.merchantUid().split(":")[0].equals("UPDATE")) {
                    paymentService.updateCard(command);
               } else {
                    paymentService.nextSchedule(command);
               }
          } else if (command.status().equals("failed")) {
               paymentService.failedSchedule(command);
          }

          return Result.ok("okokok");
     }

     @PostMapping("/api/retry/schedule")
     ResponseEntity<ApiResult> retrySchedule(
          @Valid @RequestBody ScheduleRequest payload
     ) {
          List<ScheduleResponse> response = paymentClient.schedule(List.of(payload));

          if (response == null || response.isEmpty() || !response.getLast().failReason().isEmpty()) {
               throw new GlobalAnseongappException("재결제 실패");
          }

          return Result.ok("ok");
     }
}
