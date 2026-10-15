package com.anseong.core.infrastructure.client.messanger;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import com.anseong.core.domain.aop.fallback.FallbackToQueue;
import com.anseong.core.domain.client.messanger.MessangerClient;
import com.anseong.core.domain.client.messanger.MessangerPlatform;
import com.anseong.core.domain.client.messanger.request.MessangerRequest;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;

@Component
@Primary
@Retry(name = "messanger")
@CircuitBreaker(name = "messanger")
@FallbackToQueue(queueName = "messanger")
class MessangerClientFactory implements MessangerClient {

     private final Map<MessangerPlatform, MessangerClient> clients;
     private final MessangerPlatform DEFAULT_PLATFORM = MessangerPlatform.NAVER;

     MessangerClientFactory(List<InternalMessangerClient> notificationClients) {
          this.clients = notificationClients.stream()
               .collect(Collectors.toMap(
                    InternalMessangerClient::supports,
                    Function.identity()
               ));
     }

     private MessangerClient getClient(MessangerPlatform platform) {
          if (platform == null)
               platform = DEFAULT_PLATFORM;

          return clients.get(platform);
     }

     @Override
     public void sendReportSMSNotificationToMe(MessangerRequest request) {
          getClient(request.platform()).sendReportSMSNotificationToMe(request);
     }
}
