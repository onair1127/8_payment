package com.anseong.core.domain.client.messanger.exception;

import com.anseong.core.domain.exception.GlobalAnseongappException;

public class MessangerSendFailedException extends GlobalAnseongappException {

     private static final long serialVersionUID = 1212879617583912831L;

     public MessangerSendFailedException(String message) {
          super(message);
     }

     public MessangerSendFailedException(String message, Throwable cause) {
          super(message, cause);
     }
}
