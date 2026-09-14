package com.inventorymanagement.backend.domain.exception;

public class BlockedUserException extends RuntimeException {

    public BlockedUserException(String message) {
        super(message);
    }
}