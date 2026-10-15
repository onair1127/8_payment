package com.anseong.core.domain.model.payment_failed;

import java.time.LocalDateTime;
import java.util.Objects;

import com.anseong.core.domain.client.payment.response.PaymentResponse;
import com.anseong.core.domain.model.AbstractBaseEntity;
import com.anseong.core.domain.model.user.UserId;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "payment_failed")
public class PaymentFailed extends AbstractBaseEntity {

     private static final long serialVersionUID = 7595939536889355086L;

     @Id
     @GeneratedValue(strategy = GenerationType.IDENTITY)
     private long id;

     @Column(name = "user_id")
     private long userId;

     @Column(name = "status")
     private String status;

     @Column(name = "buyer_name")
     private String buyerName;

     @Column(name = "buyer_tel")
     private String buyerTel;

     @Column(name = "failed_reason")
     private String failedReason;

     @Column(name = "cancel_reason")
     private String cancelReason;

     @Column(name = "created_date")
     private LocalDateTime createdDate;

     PaymentFailed() {}

     public static PaymentFailed create(UserId userId, PaymentResponse failedPaymentResponse) {
          PaymentFailed failed = new PaymentFailed();
          failed.userId = userId.value();
          failed.status = failedPaymentResponse.status();
          failed.buyerName = failedPaymentResponse.buyerName();
          failed.buyerTel = failedPaymentResponse.buyerTel();
          failed.failedReason = failedPaymentResponse.failReason();
          failed.cancelReason = failedPaymentResponse.cancelReason();
          failed.createdDate = LocalDateTime.now();

          return failed;
     }

     public PaymentId getId() {
          return new PaymentId(this.id);
     }

     public UserId getUserId() {
          return new UserId(this.userId);
     }

     public String getStatus() {
          return this.status;
     }

     public String getBuyerName() {
          return this.buyerName;
     }

     public String getBuyerTel() {
          return this.buyerTel;
     }

     public String getFailedReason() {
          return this.failedReason;
     }

     public String getCancelReason() {
          return this.cancelReason;
     }

     public LocalDateTime getCreatedDate() {
          return this.createdDate;
     }

     @Override
     public boolean equals(Object o) {
          if (o == this)
               return true;
          if (!(o instanceof PaymentFailed)) {
               return false;
          }
          PaymentFailed paymentFailed = (PaymentFailed) o;
          return id == paymentFailed.id && userId == paymentFailed.userId && Objects.equals(status, paymentFailed.status) && Objects.equals(buyerName, paymentFailed.buyerName) && Objects.equals(buyerTel, paymentFailed.buyerTel) && Objects.equals(failedReason, paymentFailed.failedReason) && Objects.equals(cancelReason, paymentFailed.cancelReason) && Objects.equals(createdDate, paymentFailed.createdDate);
     }

     @Override
     public int hashCode() {
          return Objects.hash(id, userId, status, buyerName, buyerTel, failedReason, cancelReason, createdDate);
     }

     @Override
     public String toString() {
          return "{" +
               ", status='" + getStatus() + "'" +
               ", buyerName='" + getBuyerName() + "'" +
               ", buyerTel='" + getBuyerTel() + "'" +
               ", failedReason='" + getFailedReason() + "'" +
               ", cancelReason='" + getCancelReason() + "'" +
               ", createdDate='" + getCreatedDate() + "'" +
               "}";
     }
}
