package com.anseong.core.domain.client.payment.response;

import com.anseong.core.domain.model.user.UserId;

public record ScheduleResponse(
     UserId userId
     , String customerUid
     , String merchantUid
     , String buyerName
     , String buyerEmail
     , String buyerTel
     , String buyerAddr
     , String failReason
     , String scheduleStatus
     , long scheduleAt
) {
     public static Builder builder() {
          return new Builder();
     }

     public static class Builder {
          private UserId userId;
          private String customerUid;
          private String merchantUid;
          private String buyerName;
          private String buyerEmail;
          private String buyerTel;
          private String buyerAddr;
          private String failReason;
          private String scheduleStatus;
          private long scheduleAt;

          public Builder userId(UserId userId) {
               this.userId = userId;
               return this;
          }

          public Builder customerUid(String customerUid) {
               this.customerUid = customerUid;
               return this;
          }

          public Builder merchantUid(String merchantUid) {
               this.merchantUid = merchantUid;
               return this;
          }

          public Builder buyerName(String buyerName) {
               this.buyerName = buyerName;
               return this;
          }

          public Builder buyerEmail(String buyerEmail) {
               this.buyerEmail = buyerEmail;
               return this;
          }

          public Builder buyerTel(String buyerTel) {
               this.buyerTel = buyerTel;
               return this;
          }

          public Builder buyerAddr(String buyerAddr) {
               this.buyerAddr = buyerAddr;
               return this;
          }

          public Builder failReason(String failReason) {
               this.failReason = failReason;
               return this;
          }

          public Builder scheduleStatus(String scheduleStatus) {
               this.scheduleStatus = scheduleStatus;
               return this;
          }

          public Builder scheduleAt(long scheduleAt) {
               this.scheduleAt = scheduleAt;
               return this;
          }

          public ScheduleResponse build() {
               return new ScheduleResponse(userId, customerUid, merchantUid, buyerName, buyerEmail, buyerTel, buyerAddr, failReason, scheduleStatus, scheduleAt);
          }
     }
}
