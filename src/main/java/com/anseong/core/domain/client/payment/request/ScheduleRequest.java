package com.anseong.core.domain.client.payment.request;

import java.time.ZonedDateTime;

import com.anseong.core.domain.client.payment.PaymentPlatform;
import com.anseong.core.domain.client.payment.response.PaymentResponse;
import com.anseong.core.domain.model.user.UserId;

public record ScheduleRequest(
     PaymentPlatform platform
     , UserId userId
     , String uid
     , String customerUid
     , String merchantUid
     , String name
     , long amount
     , String buyerName
     , String buyerEmail
     , String buyerTel
     , String buyerAddr
     , ZonedDateTime scheduleAt
) {
     public ScheduleRequest withMerchantUidAndScheduleAt(String merchantUid, ZonedDateTime scheduleAt) {
          return new ScheduleRequest(
               this.platform
               , this.userId
               , this.uid
               , this.customerUid
               , merchantUid
               , this.name
               , this.amount
               , this.buyerName
               , this.buyerEmail
               , this.buyerTel
               , this.buyerAddr
               , scheduleAt
          );
     }

     public ScheduleRequest withMerchantUid(String merchantUid) {
          return new ScheduleRequest(
               this.platform
               , this.userId
               , this.uid
               , this.customerUid
               , merchantUid
               , this.name
               , this.amount
               , this.buyerName
               , this.buyerEmail
               , this.buyerTel
               , this.buyerAddr
               , this.scheduleAt
          );
     }

     public static ScheduleRequest of(PaymentPlatform platform, PaymentResponse paymentResponse, String merchantUid, ZonedDateTime scheduleAt) {
          return builder()
               .platform(platform)
               .userId(paymentResponse.userId())
               .uid(paymentResponse.uid())
               .customerUid(paymentResponse.customerUid())
               .merchantUid(merchantUid)
               .name(paymentResponse.name())
               .amount(paymentResponse.amount())
               .buyerName(paymentResponse.buyerName())
               .buyerEmail(paymentResponse.buyerEmail())
               .buyerTel(paymentResponse.buyerTel())
               .buyerAddr(paymentResponse.buyerAddr())
               .scheduleAt(scheduleAt)
               .build();
     }

     public static Builder builder() {
          return new Builder();
     }

     public static class Builder {
          private PaymentPlatform platform;
          private UserId userId;
          private String uid;
          private String customerUid;
          private String merchantUid;
          private String name;
          private long amount;
          private String buyerName;
          private String buyerEmail;
          private String buyerTel;
          private String buyerAddr;
          private ZonedDateTime scheduleAt;

          public Builder platform(PaymentPlatform platform) {
               this.platform = platform;
               return this;
          }

          public Builder userId(UserId userId) {
               this.userId = userId;
               return this;
          }

          public Builder uid(String uid) {
               this.uid = uid;
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

          public Builder name(String name) {
               this.name = name;
               return this;
          }

          public Builder amount(long amount) {
               this.amount = amount;
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

          public Builder scheduleAt(ZonedDateTime scheduleAt) {
               this.scheduleAt = scheduleAt;
               return this;
          }

          public ScheduleRequest build() {
               return new ScheduleRequest(platform, userId, uid, customerUid, merchantUid, name, amount, buyerName, buyerEmail, buyerTel, buyerAddr, scheduleAt);
          }
     }
}
