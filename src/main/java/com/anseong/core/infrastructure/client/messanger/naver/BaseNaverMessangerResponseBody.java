package com.anseong.core.infrastructure.client.messanger.naver;

interface BaseNaverNotifiactionResponseBody {
     String requestId();
     String requestTime();
     String statusCode();
     String statusName();
}
