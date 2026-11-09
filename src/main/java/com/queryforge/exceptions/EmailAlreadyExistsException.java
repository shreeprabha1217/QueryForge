package com.queryforge.exceptions;

public class EmailAlreadyExistsException extends RuntimeException  {
      public EmailAlreadyExistsException(String message) {
        super(message);
    }
}
