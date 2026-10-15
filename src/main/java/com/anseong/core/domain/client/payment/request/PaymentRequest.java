package com.anseong.core.domain.client.payment.request;

import com.anseong.core.domain.client.payment.PaymentPlatform;
import com.anseong.core.domain.model.user.UserId;

public record PaymentRequest(
     PaymentPlatform platform
     , UserId userId
     , String uid
     , String customerUid
     , String merchantUid
     , long amount
     , String buyerName
     , String buyerEmail
     , String buyerTel
     , String buyerAddr
) {
     public static Builder builder() {
          return new Builder();
     }

     public static class Builder {
          private PaymentPlatform platform;
          private UserId userId;
          private String uid;
          private String customerUid;
          private String merchantUid;
          private long amount;
          private String buyerName;
          private String buyerEmail;
          private String buyerTel;
          private String buyerAddr;

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

          public PaymentRequest build() {
               return new PaymentRequest(platform, userId, uid, customerUid, merchantUid, amount, buyerName, buyerEmail, buyerTel, buyerAddr);
          }
     }
}
