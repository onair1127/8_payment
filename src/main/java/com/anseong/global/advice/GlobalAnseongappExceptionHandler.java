package com.anseong.global.advice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.anseong.core.domain.exception.GlobalAnseongappException;

@ControllerAdvice
public class GlobalAnseongappExceptionHandler {

     private static final Logger log = LoggerFactory.getLogger("ERROR-GLOBAL-LOGGER");

     @ExceptionHandler(GlobalAnseongappException.class)
     void handleApplicationException(GlobalAnseongappException e) {
          log.warn("비즈니스 로직 실패 -> [{} : {}]", e.getMessage(), e.getCause());
     }

     @ExceptionHandler(Exception.class)
     void handleUnexpectedException(Exception e) {
          log.error("!!! 예측 못한 에러 발생 -> [{} : {}]", e.getMessage(), e.getCause());
     }
}
