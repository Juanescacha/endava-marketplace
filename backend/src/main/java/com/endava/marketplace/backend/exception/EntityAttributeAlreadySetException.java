package com.endava.marketplace.backend.exception;

public class EntityAttributeAlreadySetException extends RuntimeException {
    public EntityAttributeAlreadySetException(String message) {
        super(message);
    }
}
