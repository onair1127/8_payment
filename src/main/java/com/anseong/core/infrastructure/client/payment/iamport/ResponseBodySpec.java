package com.anseong.core.infrastructure.client.payment.iamport;

import com.anseong.core.domain.model.user.UserId;

interface IamportResponseBody {

}

interface BaseIamportResponseBody<T> extends IamportResponseBody {
     /**
      * 포트원 거래고유번호
      * 결제건의 포트원 거래고유번호
      */
     String impUid();

     /**
      * 구매자의 결제 수단 식별 고유번호
      * 빌링키와 매핑되며 고객사에서 채번하는 구매자의 결제 수단 식별 고유번호
      */
     String customerUid();

     /**
      * 고객사 주문번호
      * 결제건의 고객사 주문번호
      */
     String merchantUid();

     /**
      * 제품명
      * 결제건의 제품명
      */
     String name();

     /**
      * 결제금액
      * 결제건의 결제금액
      */
     long amount();

     /**
      * 주문자명
      * 결제건의 주문자명
      */
     String buyerName();

     /**
      * 주문자 Email주소
      * 결제건의 주문자의 Email주소
      */
     String buyerEmail();

     /**
      * 주문자 전화번호
      * 결제건의 주문자 전화번호
      */
     String buyerTel();

     /**
      * 주문자 주소
      * 결제건의 주문자 주소
      */
     String buyerAddr();

     /**
      * 추가정보
      * 결제 요청시 고객사에서 전달한 추가정보 (JSON string으로 전달)
      */
     String customData();

     /**
      * 결제실패 사유
      * 결제상태가 결제실패(failed)가 아닐 경우 null로 표시됩니다.
      */
     String failReason();

     /**
      * 도메인 객체로 반환합니다.
      */
     T toDomainResponse(UserId userId);
}
