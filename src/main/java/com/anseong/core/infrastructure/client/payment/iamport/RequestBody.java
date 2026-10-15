package com.anseong.core.infrastructure.client.payment.iamport;

import java.util.List;

import com.anseong.core.domain.client.payment.request.ScheduleRequest;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

final class RequestBody {
     @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
     record Schedule(
          String customerUid,
          List<Schedules> schedules
     ) {
          @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
          record Schedules(
               String merchantUid,
               String name,
               String buyerName,
               String buyerTel,
               long amount,
               long scheduleAt
          ) {
               static Builder builder() {
                    return new Builder();
               }
               static class Builder {
                    private String merchantUid;
                    private String name;
                    private String buyerName;
                    private String buyerTel;
                    private long amount;
                    private long scheduleAt;
                    Builder merchantUid(String merchantUid) {
                         this.merchantUid = merchantUid;
                         return this;
                    }
                    Builder name(String name) {
                         this.name = name;
                         return this;
                    }
                    Builder buyerName(String buyerName) {
                         this.buyerName = buyerName;
                         return this;
                    }
                    Builder buyerTel(String buyerTel) {
                         this.buyerTel = buyerTel;
                         return this;
                    }
                    Builder amount(long amount) {
                         this.amount = amount;
                         return this;
                    }
                    Builder scheduleAt(long scheduleAt) {
                         this.scheduleAt = scheduleAt;
                         return this;
                    }
                    Schedules build() {
                         return new Schedules(merchantUid, name, buyerName, buyerTel, amount, scheduleAt);
                    }
               }
          }
          static Schedule from(List<ScheduleRequest> scheduleRequests) {
               String customerUid = scheduleRequests.get(0).customerUid();
               return Schedule.builder()
                    .customerUid(customerUid)
                    .schedules(scheduleRequests.stream()
                         .map(e -> Schedules.builder()
                              .merchantUid(e.merchantUid())
                              .scheduleAt(e.scheduleAt().toEpochSecond())
                              .amount(e.amount())
                              .name(e.name())
                              .buyerName(e.buyerName())
                              .buyerTel(e.buyerTel())
                              .build()
                         ).toList()
                    ).build();
          }
          static Builder builder() {
               return new Builder();
          }
          static class Builder {
               private String customerUid;
               private List<Schedules> schedules;
               Builder customerUid(String customerUid) {
                    this.customerUid = customerUid;
                    return this;
               }
               Builder schedules(List<Schedules> schedules) {
                    this.schedules = schedules;
                    return this;
               }
               Schedule build() {
                    return new Schedule(customerUid, schedules);
               }
          }
     }

     @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
     record Unschedule(
          String customerUid,
          String merchantUid
     ) {
          static Unschedule from(ScheduleRequest request) {
               return builder()
                    .customerUid(request.customerUid())
                    .merchantUid(request.merchantUid())
                    .build();
          }

          static Builder builder() {
               return new Builder();
          }

          static class Builder {
               private String customerUid;
               private String merchantUid;

               public Builder customerUid(String customerUid) {
                    this.customerUid = customerUid;
                    return this;
               }

               public Builder merchantUid(String merchantUid) {
                    this.merchantUid = merchantUid;
                    return this;
               }

               public Unschedule build() {
                    return new Unschedule(customerUid, merchantUid);
               }
          }
     }
}
