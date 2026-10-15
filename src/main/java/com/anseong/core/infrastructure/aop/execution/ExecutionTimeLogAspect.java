package com.anseong.core.infrastructure.aop.execution;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import com.anseong.core.domain.aop.execution.ExecutionTimeLog;

@Component
@Aspect
public class ExecutionTimeLogAspect {

     private static final Logger log = LoggerFactory.getLogger(ExecutionTimeLogAspect.class);

     @Around("@annotation(executionTimeLog)")
     public Object executionTimeLog(ProceedingJoinPoint joinPoint, ExecutionTimeLog executionTimeLog) throws Throwable {
          log.info("메소드 시간 측정 시작");

          StopWatch stopWatch = new StopWatch();
          stopWatch.start(joinPoint.toShortString());

          Object proceed = joinPoint.proceed();

          stopWatch.stop();
          log.info(stopWatch.shortSummary() + ", " + joinPoint.toShortString());

          return proceed;
     }
}
