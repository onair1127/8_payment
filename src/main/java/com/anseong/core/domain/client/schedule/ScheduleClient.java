package com.anseong.core.domain.client.schedule;

import java.util.concurrent.CompletableFuture;

import com.anseong.core.domain.client.schedule.request.ScheduleEventRequest;
import com.anseong.core.domain.client.schedule.request.ScheduleMessageQueueRequest;

public interface ScheduleClient {
     CompletableFuture<Void> scheduleEventAsync(ScheduleEventRequest request);

     CompletableFuture<Void> scheduleToMessageQueue(ScheduleMessageQueueRequest request);
}
