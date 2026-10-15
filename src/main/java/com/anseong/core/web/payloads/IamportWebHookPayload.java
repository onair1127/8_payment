package com.anseong.core.web.payloads;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import com.anseong.core.application.commands.payment.PaymentWebHookCommand;
import com.anseong.core.domain.client.payment.PaymentPlatform;
import com.fasterxml.jackson.annotation.JsonProperty;

public class IamportWebHookPayload {

     @NotNull
     @NotEmpty
     @JsonProperty("imp_uid")
     private String impUid;

     @NotNull
     @NotEmpty
     @JsonProperty("merchant_uid")
     private String merchantUid;

     @NotNull
     @NotEmpty
     @JsonProperty("status")
     private String status;

     public PaymentWebHookCommand toCommand() {
          return new PaymentWebHookCommand(PaymentPlatform.IAMPORT, impUid, merchantUid, status);
     }

     public void setImpUID(String impUid) {
          this.impUid = impUid;
     }

     public void setMerchantUID(String merchantUid) {
          this.merchantUid = merchantUid;
     }

     public void setStatus(String status) {
          this.status = status;
     }

     @Override
     public String toString() {
          return "{" +
               " merchantUid='" + merchantUid + "'" +
               ", impUid='" + impUid + "'" +
               ", status='" + status + "'" +
               "}";
     }
}
