package com.anseong.core.infrastructure.client.messanger.naver;

import com.fasterxml.jackson.databind.annotation.JsonNaming;

final class ResponseBody {
     @JsonNaming
     record SMS(
          String requestId,
          String requestTime,
          String statusCode,
          String statusName
     ) implements BaseNaverNotifiactionResponseBody {
          boolean isSuccess() {
               if (statusCode.equals("202") && statusName.equals("success"))
                    return true;
               else
                    return false;
          }
     }
}
