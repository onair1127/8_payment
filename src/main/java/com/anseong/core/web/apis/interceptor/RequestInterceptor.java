package com.anseong.core.web.apis.interceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.lang.Nullable;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class RequestInterceptor implements HandlerInterceptor {

     private static final Logger log = LoggerFactory.getLogger(RequestInterceptor.class);

     @Override
     public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
               throws Exception {

          request.setAttribute("startTime", System.currentTimeMillis());
          return true;
     }

     @Override
     @SuppressWarnings("null")
     public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
               @Nullable Exception ex) throws Exception {
          long startTime = (long) request.getAttribute("startTime");
          long endTime = System.currentTimeMillis();

          String controller = "UNKNOWN";

          if (handler instanceof HandlerMethod handlerMethod) {
               controller = handlerMethod.getMethod().getName();
          }

          log.info("[{}] {} | {}, userId: {} >>> {} | {} : {}ms",
               request.getMethod(),
               request.getAttribute("url"),
               request.getAttribute("IP"),
               request.getAttribute("userId"),
               controller,
               (HttpStatus.valueOf(response.getStatus()).toString()),
               (endTime - startTime)
          );

          HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
     }
}
