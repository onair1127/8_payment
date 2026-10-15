package com.anseong.core.domain.client.schedule.exception;

import com.anseong.core.domain.exception.GlobalAnseongappException;

public class ScheduleTaskFailedException extends GlobalAnseongappException {

     public ScheduleTaskFailedException(String message) {
          super(message);
     }

     public ScheduleTaskFailedException(String message, Throwable cause) {
          super(message, cause);
     }
}
