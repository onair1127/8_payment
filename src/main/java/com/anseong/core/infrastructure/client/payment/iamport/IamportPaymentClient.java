package com.anseong.core.infrastructure.client.payment.iamport;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.ResolvableType;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import com.anseong.core.domain.client.payment.PaymentPlatform;
import com.anseong.core.domain.client.payment.request.PaymentRequest;
import com.anseong.core.domain.client.payment.request.ScheduleRequest;
import com.anseong.core.domain.client.payment.response.PaymentResponse;
import com.anseong.core.domain.client.payment.response.ScheduleResponse;
import com.anseong.core.domain.model.user.UserId;
import com.anseong.core.infrastructure.client.payment.InternalPaymentClient;
import com.anseong.core.infrastructure.client.payment.iamport.ResponseBody.Payment;
import com.anseong.core.infrastructure.client.payment.iamport.ResponseBody.Schedule;
import com.anseong.global.utils.JsonUtils;

@Component
class IamportPaymentClient implements InternalPaymentClient {

     private final RestClient iamportRestClient;

     IamportPaymentClient(@Qualifier("iamportRestClient") RestClient iamportRestClient) {
          this.iamportRestClient = iamportRestClient;
     }

     @Override
     public PaymentResponse getPayment(PaymentRequest request) {
          UserId userId = request.userId();
          IamportPayload<Payment> responseBody = iamportRestClient
               .get()
               .uri("/payments/" + request.uid())
               .retrieve()
               .onStatus(HttpStatusCode::is5xxServerError, (req, res) -> throw5xxError(userId, res, IamportPaymentErrorType.GET_PAYMENTS_FAILED))
               .body(getTypeReference(ResponseBody.Payment.class));

          if (responseBody == null)
               throw new IamportPaymentFailedException(userId, IamportPaymentErrorType.GET_PAYMENTS_FAILED, HttpStatus.ACCEPTED, "응답 바디가 비어있음.");

          Payment response = responseBody.response(userId);
          return response.toDomainResponse(userId);
     }

     @Override
     public ScheduleResponse getSchedule(ScheduleRequest request) {
          UserId userId = request.userId();
          IamportPayload<Schedule> responseBody = iamportRestClient
               .get()
               .uri("/subscribe/payments/schedule/" + request.merchantUid())
               .retrieve()
               .onStatus(HttpStatusCode::isError, (req, res) -> throwError(userId, res, IamportPaymentErrorType.GET_SCHEDULE_FAILED))
               .body(getTypeReference(ResponseBody.Schedule.class));

          if (responseBody == null)
               throw new IamportPaymentFailedException(userId, IamportPaymentErrorType.GET_SCHEDULE_FAILED, HttpStatus.ACCEPTED, "응답 바디가 비어있음.");

          Schedule response = responseBody.response(userId);
          return response.toDomainResponse(userId);
     }

     @Override
     public List<ScheduleResponse> getCustomerSchedules(ScheduleRequest request) {
          UserId userId = request.userId();
          IamportMultipleResponsePayload<Schedule> responseBody = iamportRestClient
               .get()
               .uri("/subscribe/payments/schedule/customers/" + request.customerUid())
               .retrieve()
               .onStatus(HttpStatusCode::isError, (req, res) -> throwError(userId, res, IamportPaymentErrorType.GET_SCHEDULE_FAILED))
               .body(getListTypeReference(ResponseBody.Schedule.class));

          if (responseBody == null)
               throw new IamportPaymentFailedException(userId, IamportPaymentErrorType.GET_SCHEDULE_FAILED, HttpStatus.ACCEPTED, "응답 바디가 비어있음.");

          List<Schedule> responses = responseBody.response(userId);
          return responses.stream()
               .map(e -> e.toDomainResponse(userId))
               .toList();
     }

     @Override
     public List<ScheduleResponse> schedule(List<ScheduleRequest> requests) {
          UserId userId = requests.get(0).userId();
          RequestBody.Schedule requestBody = RequestBody.Schedule.from(requests);

          IamportMultipleResponsePayload<Schedule> responseBody = iamportRestClient
               .post()
               .uri("/subscribe/payments/schedule")
               .body(requestBody)
               .retrieve()
               .onStatus(HttpStatusCode::isError, (req, res) -> throwError(userId, res, IamportPaymentErrorType.SCHEDULE_FAILED))
               .body(getListTypeReference(ResponseBody.Schedule.class));

          if (responseBody == null)
               throw new IamportPaymentFailedException(userId, IamportPaymentErrorType.SCHEDULE_FAILED, HttpStatus.ACCEPTED, "응답 바디가 비어있음.");

          List<Schedule> responses = responseBody.response(userId);
          return responses.stream()
               .map(e -> e.toDomainResponse(userId))
               .toList();
     }

     @Override
     public ScheduleResponse unschedule(ScheduleRequest request) {
          UserId userId = request.userId();
          RequestBody.Unschedule requestBody = RequestBody.Unschedule.from(request);
          IamportPayload<Schedule> responseBody = iamportRestClient.post()
               .uri("/subscribe/payments/unschedule")
               .body(requestBody)
               .retrieve()
               .onStatus(HttpStatusCode::isError, (req, res) -> throwError(userId, res, IamportPaymentErrorType.UNSCHEDULE_FAILED))
               .body(getTypeReference(ResponseBody.Schedule.class));

          if (responseBody == null)
               throw new IamportPaymentFailedException(userId, IamportPaymentErrorType.UNSCHEDULE_FAILED, HttpStatus.ACCEPTED, "응답 바디가 비어있음.");

          Schedule response = responseBody.response(userId);
          return response.toDomainResponse(request.userId());
     }

     @Override
     public List<ScheduleResponse> unscheduleCustomerAll(ScheduleRequest request) {
          UserId userId = request.userId();
          IamportMultipleResponsePayload<Schedule> responseBody = iamportRestClient.post()
               .uri("/subscribe/payments/unschedule")
               .body(Map.of("customer_uid", request.customerUid()))
               .retrieve()
               .onStatus(HttpStatusCode::isError, (req, res) -> throwError(userId, res, IamportPaymentErrorType.UNSCHEDULE_FAILED))
               .body(getListTypeReference(ResponseBody.Schedule.class));

          if (responseBody == null)
               throw new IamportPaymentFailedException(userId, IamportPaymentErrorType.UNSCHEDULE_FAILED, HttpStatus.ACCEPTED, "응답 바디가 비어있음.");

          List<Schedule> responses = responseBody.response(userId);
          return responses.stream()
               .map(e -> e.toDomainResponse(userId))
               .toList();
     }

     @Override
     public PaymentPlatform supports() {
          return PaymentPlatform.IAMPORT;
     }

     private <T extends IamportResponseBody> ParameterizedTypeReference<IamportPayload<T>> getTypeReference(Class<T> bodyClassType) {
          Type type = ResolvableType.forClassWithGenerics(
               IamportPayload.class,
               bodyClassType
          ).getType();

          return ParameterizedTypeReference.forType(type);
     }

     private <T extends IamportResponseBody> ParameterizedTypeReference<IamportMultipleResponsePayload<T>> getListTypeReference(Class<T> bodyClassType) {
          ResolvableType listBodyClassType = ResolvableType.forClassWithGenerics(List.class, bodyClassType);

          Type type = ResolvableType.forClassWithGenerics(
               IamportMultipleResponsePayload.class,
               listBodyClassType
          ).getType();

          return ParameterizedTypeReference.forType(type);
     }

     private void throwError(UserId userId, ClientHttpResponse errorResponse, IamportPaymentErrorType errorType) throws IOException {
          IamportErrorResponse response = JsonUtils.toObject(errorResponse.getBody(), IamportErrorResponse.class);
          String errorMessage;

          if (response != null)
               errorMessage = response.message();
          else
               errorMessage = "알 수 없는 에러 : " + errorResponse.getStatusText();

          throw new IamportPaymentFailedException(userId, errorType, errorResponse.getStatusCode(), errorMessage);
     }

     private void throw5xxError(UserId userId, ClientHttpResponse errorResponse, IamportPaymentErrorType errorType) throws IOException {
          IamportErrorResponse response = JsonUtils.toObject(errorResponse.getBody(), IamportErrorResponse.class);
          String errorMessage;

          if (response != null)
               errorMessage = response.message();
          else
               errorMessage = "알 수 없는 에러 : " + errorResponse.getStatusText();

          throw new IamportPaymentFailedException(userId, errorType, errorResponse.getStatusCode(), errorMessage);
     }
}
