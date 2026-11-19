package com.queryforge.exceptions;

public class DatasetNameAlreadyExistsException extends RuntimeException {

    public DatasetNameAlreadyExistsException(String message) {
        super(message);
    }
}
