package com.anseong.core.domain.exception;

public class GlobalAnseongappException extends RuntimeException {

     private static final long serialVersionUID = -2550621968712483065L;

     public GlobalAnseongappException(String message) {
          super(message);
     }

     public GlobalAnseongappException(String message, Throwable cause) {
          super(message, cause);
     }
}
