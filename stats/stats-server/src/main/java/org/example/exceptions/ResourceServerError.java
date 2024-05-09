package org.example.exceptions;

public class ResourceServerError extends RuntimeException {
    public ResourceServerError(String message) {
        super(message);
    }
}
