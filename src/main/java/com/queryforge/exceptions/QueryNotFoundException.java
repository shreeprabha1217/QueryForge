package com.queryforge.exceptions;

public class QueryNotFoundException extends RuntimeException {

    public QueryNotFoundException(String message) {
        super(message);
    }
}
