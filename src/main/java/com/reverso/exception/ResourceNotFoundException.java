package com.reverso.exception;

public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }

    public ResourceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public ResourceNotFoundException(String message, Object identifier) {
        super(message + ": " + identifier);
    }

    public ResourceNotFoundException(String message, String field, Object identifier) {
        super(message + " (" + field + ": " + identifier + ")");
    }
}
