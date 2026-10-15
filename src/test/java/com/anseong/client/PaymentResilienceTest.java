package com.anseong.client;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.anseong.core.domain.client.payment.PaymentClient;
import com.anseong.core.domain.client.payment.PaymentPlatform;
import com.anseong.core.domain.client.payment.request.PaymentRequest;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryRegistry;

@SpringBootTest(properties = "spring.profiles.active=test")
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PaymentResilienceTest {

     @Autowired
     private PaymentClient paymentClient;

     @Autowired
     private RetryRegistry retryRegistry;

     @Autowired
     private CircuitBreakerRegistry circuitBreakerRegistry;

     @AfterEach
     public void setUp() {
          retryRegistry.remove("payment");
          circuitBreakerRegistry.remove("payment");
     }

     @Test
     @Order(1)
     @DisplayName("Retry 로직이 잘 작동되며 설정한 maxAttempts만큼 재시도가 성공적으로 완수했는지 확인합니다.")
     public void retryTest() {

          // given
          PaymentRequest request = PaymentRequest.builder()
               .platform(PaymentPlatform.TEST)
               .build();

          // when
          try {
               paymentClient.getPayment(request);
          } catch (Exception e) {
               System.out.println("Finish Throwing : " + e.getMessage());
          }

          // then
          Retry retry = retryRegistry.retry("payment");
          int maxAttempts = retry.getRetryConfig().getMaxAttempts();
          long failedBusinessCalls = retry.getMetrics().getNumberOfFailedCallsWithRetryAttempt();
          long totalRetryCount = retry.getMetrics().getNumberOfTotalCalls();
          retry.getMetrics().getNumberOfTotalCalls();

          assertThat(failedBusinessCalls).isEqualTo(1);
          assertThat(totalRetryCount).isEqualTo(maxAttempts);
     }

     @Test
     @Order(2)
     @DisplayName("지속적인 실패 발생 시 CircuitBreaker가 열려야 합니다.")
     public void circuitBreakerTest() {

          // given
          CircuitBreaker circuitBreaker = circuitBreakerRegistry.circuitBreaker("payment");
          PaymentRequest request = PaymentRequest.builder()
               .platform(PaymentPlatform.TEST)
               .build();

          // when
          assertThat(circuitBreaker.getState()).isEqualByComparingTo(CircuitBreaker.State.CLOSED);
          for (int i = 0; i < 3; i++) {
               try {
                    paymentClient.getPayment(request);
               } catch (Exception e) {
                    System.out.println("Finish Throwing : " + e.getMessage());
               }
          }

          //then
          assertThat(circuitBreaker.getState()).isEqualByComparingTo(CircuitBreaker.State.OPEN);
     }

     @Test
     @Order(3)
     @DisplayName("CircuitBreaker가 열리면 이후 로직은 Fallback Aspect가 처리해야 하며 현재 테스트시 null을 반환해야 합니다.")
     public void fallbackToQueueTest() {
          // given
          CircuitBreaker circuitBreaker = circuitBreakerRegistry.circuitBreaker("payment");
          PaymentRequest request = PaymentRequest.builder()
               .platform(PaymentPlatform.TEST)
               .build();

          Object paymentResponse = "something";

          // when
          assertThat(circuitBreaker.getState()).isEqualByComparingTo(CircuitBreaker.State.CLOSED);
          for (int i = 0; i < 3; i++) {
               paymentResponse = paymentClient.getPayment(request);
          }

          assertThat(circuitBreaker.getState()).isEqualByComparingTo(CircuitBreaker.State.OPEN);

          //then
          assertThat(paymentResponse).isNull();
     }
}
