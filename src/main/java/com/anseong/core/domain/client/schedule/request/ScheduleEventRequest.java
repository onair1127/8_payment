package com.anseong.core.domain.client.schedule.request;

import java.time.LocalDateTime;

import com.anseong.core.domain.client.schedule.SchedulePlatform;

public record ScheduleEventRequest(
     SchedulePlatform platform,
     String scheduleName,
     LocalDateTime scheduleAt,
     String payload,
     String arn
) {
     public static Builder builder() {
          return new Builder();
     }

     public static class Builder {
          private SchedulePlatform platform;
          private String scheduleName;
          private LocalDateTime scheduleAt;
          private String payload;
          private String arn;

          public Builder platform(SchedulePlatform platform) {
               this.platform = platform;
               return this;
          }

          public Builder scheduleName(String scheduleName) {
               this.scheduleName = scheduleName;
               return this;
          }

          public Builder scheduleAt(LocalDateTime scheduleAt) {
               this.scheduleAt = scheduleAt;
               return this;
          }

          public Builder payload(String payload) {
               this.payload = payload;
               return this;
          }

          public Builder arn(String arn) {
               this.arn = arn;
               return this;
          }

          public ScheduleEventRequest build() {
               return new ScheduleEventRequest(platform, scheduleName, scheduleAt, payload, arn);
          }
     }
}
