package com.anseong.core.infrastructure.client.messanger.naver;

import java.util.List;

import com.anseong.core.domain.client.messanger.request.MessangerRequest;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

final class RequestBody {

     @JsonNaming(PropertyNamingStrategies.LowerCamelCaseStrategy.class)
     record SMS(
          String type,
          String from,
          String content,
          List<Message> messages
     ) {
          static SMS toMe(MessangerRequest request, String content) {
               return SMS.builder()
                    .type(request.type().name())
                    .from("01020092755")
                    .content(content)
                    .messages(Message.builder()
                         .to("01020092755")
                         .build()
                    )
                    .build();
          }

          @JsonNaming(PropertyNamingStrategies.LowerCamelCaseStrategy.class)
          record Message(
               String to,
               String subject,
               String content
          ) {
               static Builder builder() {
                    return new Builder();
               }

               static class Builder {
                    private String to;
                    private String subject;
                    private String content;

                    Builder to(String to) {
                         this.to = to;
                         return this;
                    }

                    Builder subject(String subject) {
                         this.subject = subject;
                         return this;
                    }

                    Builder content(String content) {
                         this.content = content;
                         return this;
                    }

                    Message build() {
                         return new Message(to, subject, content);
                    }
               }
          }

          static Builder builder() {
               return new Builder();
          }

          static class Builder {
               private String type;
               private String from;
               private String content;
               private List<Message> messages;

               Builder type(String type) {
                    this.type = type;
                    return this;
               }

               Builder from(String from) {
                    this.from = from;
                    return this;
               }

               Builder content(String content) {
                    this.content = content;
                    return this;
               }

               Builder messages(Message message) {
                    this.messages = List.of(message);
                    return this;
               }

               Builder messages(List<Message> messages) {
                    this.messages = messages;
                    return this;
               }

               SMS build() {
                    return new SMS(type, from, content, messages);
               }
          }
     }
}
