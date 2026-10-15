package com.anseong.core.domain.client.messanger.request;

import com.anseong.core.domain.client.messanger.MessangerPlatform;
import com.anseong.core.domain.client.messanger.MessangerTemplate;
import com.anseong.core.domain.client.messanger.MessangerType;

public record MessangerRequest(
     MessangerPlatform platform,
     MessangerType type,
     MessangerTemplate template,
     String username,
     String email,
     String phone
) {
     public static Builder builder() {
          return new Builder();
     }

     public static class Builder {
          private MessangerPlatform platform;
          private MessangerType type;
          private MessangerTemplate template;
          private String username;
          private String email;
          private String phone;

          public Builder platform(MessangerPlatform platform) {
               this.platform = platform;
               return this;
          }

          public Builder type(MessangerType type) {
               this.type = type;
               return this;
          }

          public Builder template(MessangerTemplate template) {
               this.template = template;
               return this;
          }

          public Builder username(String username) {
               this.username = username;
               return this;
          }

          public Builder email(String email) {
               this.email = email;
               return this;
          }

          public Builder phone(String phone) {
               this.phone = phone;
               return this;
          }

          public MessangerRequest build() {
               return new MessangerRequest(platform, type, template, username, email, phone);
          }
     }
}
