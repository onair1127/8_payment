package com.anseong.core.infrastructure.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.core.retry.RetryListener;
import org.springframework.resilience.retry.MethodRetryEvent;
import org.springframework.stereotype.Component;

@Component
public class ClientRetryListener implements RetryListener {

     private static final Logger log = LoggerFactory.getLogger("RETRY-CLIENT-LOGGER");

     @EventListener
     public void onRetryableExecution(MethodRetryEvent event) {
          log.warn("클라이언트 요청 재시도 : {}", event.getFailure().getMessage());
     }
}
