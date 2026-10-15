package com.anseong.core.infrastructure.client.schedule.aws;

import java.time.LocalDateTime;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.anseong.core.domain.client.schedule.SchedulePlatform;
import com.anseong.core.domain.client.schedule.request.ScheduleEventRequest;
import com.anseong.core.domain.client.schedule.request.ScheduleMessageQueueRequest;
import com.anseong.core.infrastructure.client.schedule.InternalScheduleClient;

import software.amazon.awssdk.services.scheduler.SchedulerAsyncClient;
import software.amazon.awssdk.services.scheduler.model.ActionAfterCompletion;
import software.amazon.awssdk.services.scheduler.model.CreateScheduleRequest;
import software.amazon.awssdk.services.scheduler.model.DeadLetterConfig;
import software.amazon.awssdk.services.scheduler.model.Target;

@Component
public class AwsScheduleClient implements InternalScheduleClient {

     private final SchedulerAsyncClient awsSchedulerAsyncClient;
     private final String roleArn;
     private final String eventBridgeArn;
     private final String sqsArn;
     private final String dlqArn;

     public AwsScheduleClient(
          @Qualifier("awsSchedulerAsyncClient") SchedulerAsyncClient awsSchedulerAsyncClient,
          @Value("${api.aws.arn_role}") String roleArn,
          @Value("${api.aws.arn_eventbridge}") String eventBridgeArn,
          @Value("${api.aws.arn_sqs}") String sqsArn,
          @Value("${api.aws.arn_dlq}") String dlqArn
     ) {
          this.awsSchedulerAsyncClient = awsSchedulerAsyncClient;
          this.roleArn = roleArn;
          this.eventBridgeArn = eventBridgeArn;
          this.sqsArn = sqsArn;
          this.dlqArn = dlqArn;
     }

     @Override
     public CompletableFuture<Void> scheduleEventAsync(ScheduleEventRequest request) {
          String startTime = LocalDateTime.now().toString();

          CreateScheduleRequest scheduleRequest = CreateScheduleRequest.builder()
               .name("SCHEDULE-EVENT-" + request.scheduleName() + ":" + startTime)
               .scheduleExpression("at(" + request.scheduleAt().toString() + ")")
               .target(target(eventBridgeArn, request.payload()))
               .actionAfterCompletion(ActionAfterCompletion.DELETE)
               .build();

          return awsSchedulerAsyncClient.createSchedule(scheduleRequest).thenRun(() -> {});
     }

     @Override
     public CompletableFuture<Void> scheduleToMessageQueue(ScheduleMessageQueueRequest request) {
          String startTime = LocalDateTime.now().toString();

          CreateScheduleRequest scheduleRequest = CreateScheduleRequest.builder()
               .name("SCHEDULE-SQS-" + request.scheduleName() + ":" + startTime)
               .scheduleExpression("at(" + request.scheduleAt().toString() + ")")
               .target(target(sqsArn, request.payload()))
               .actionAfterCompletion(ActionAfterCompletion.DELETE)
               .build();

          return awsSchedulerAsyncClient.createSchedule(scheduleRequest).thenRun(() -> {});
     }

     @Override
     public SchedulePlatform supports() {
          return SchedulePlatform.AWS;
     }

     private Target target(String arn, String payload) {
          return Target.builder()
               .arn(arn)
               .roleArn(roleArn)
               .input(payload)
               .deadLetterConfig(DeadLetterConfig.builder().arn(dlqArn).build())
               .build();
     }
}
