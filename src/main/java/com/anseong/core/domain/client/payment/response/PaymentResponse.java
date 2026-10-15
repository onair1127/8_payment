package com.anseong.core.domain.client.payment.response;

import com.anseong.core.domain.model.user.UserId;

public record PaymentResponse(
     UserId userId
     , String uid
     , String customerUid
     , String merchantUid
     , String status
     , String name
     , long amount
     , String buyerName
     , String buyerEmail
     , String buyerTel
     , String buyerAddr
     , String failReason
     , String cancelReason
     , String receiptUrl
     , boolean cashReceiptIssued
) {
     public static Builder builder() {
          return new Builder();
     }

     public static class Builder {
          private UserId userId;
          private String uid;
          private String customerUid;
          private String merchantUid;
          private String status;
          private String name;
          private long amount;
          private String buyerName;
          private String buyerEmail;
          private String buyerTel;
          private String buyerAddr;
          private String failReason;
          private String cancelReason;
          private String receiptUrl;
          private boolean cashReceiptIssued;

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

          public Builder status(String status) {
               this.status = status;
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

          public Builder failReason(String failReason) {
               this.failReason = failReason;
               return this;
          }

          public Builder cancelReason(String cancelReason) {
               this.cancelReason = cancelReason;
               return this;
          }

          public Builder receiptUrl(String receiptUrl) {
               this.receiptUrl = receiptUrl;
               return this;
          }

          public Builder cashReceiptIssued(boolean cashReceiptIssued) {
               this.cashReceiptIssued = cashReceiptIssued;
               return this;
          }

          public PaymentResponse build() {
               return new PaymentResponse(userId, uid, customerUid, merchantUid, status, name, amount, buyerName, buyerEmail, buyerTel, buyerAddr, failReason, cancelReason, receiptUrl, cashReceiptIssued);
          }
     }
}
