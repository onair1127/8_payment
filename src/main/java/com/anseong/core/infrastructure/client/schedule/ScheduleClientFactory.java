package com.anseong.core.infrastructure.client.schedule;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import com.anseong.core.domain.client.schedule.ScheduleClient;
import com.anseong.core.domain.client.schedule.SchedulePlatform;
import com.anseong.core.domain.client.schedule.request.ScheduleEventRequest;
import com.anseong.core.domain.client.schedule.request.ScheduleMessageQueueRequest;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;

@Component
@Primary
@Retry(name = "schedule")
@CircuitBreaker (name = "schedule")
class ScheduleClientFactory implements ScheduleClient {

     private final Map<SchedulePlatform, ScheduleClient> clients;
     private final SchedulePlatform DEFAULT_PLATFORM = SchedulePlatform.AWS;

     ScheduleClientFactory(List<InternalScheduleClient> scheduleClients) {
          this.clients = scheduleClients.stream()
               .collect(Collectors.toUnmodifiableMap(
                    InternalScheduleClient::supports,
                    Function.identity()
               ));
     }

     private ScheduleClient getClient(SchedulePlatform platform) {
          if (platform == null)
               platform = DEFAULT_PLATFORM;

          return clients.get(platform);
     }

     @Override
     public CompletableFuture<Void> scheduleEventAsync(ScheduleEventRequest request) {
          return getClient(request.platform()).scheduleEventAsync(request);
     }

     @Override
     public CompletableFuture<Void> scheduleToMessageQueue(ScheduleMessageQueueRequest request) {
          return getClient(request.platform()).scheduleToMessageQueue(request);
     }
}
