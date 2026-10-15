package com.anseong.core.domain.client.common.exception;

import com.anseong.core.domain.exception.GlobalAnseongappException;

public class AuthorizationFailedException extends GlobalAnseongappException {

     private static final long serialVersionUID = -112502224276534008L;

     public AuthorizationFailedException(String message) {
          super(message);
     }

     public AuthorizationFailedException(String message, Throwable cause) {
          super(message, cause);
     }
}
