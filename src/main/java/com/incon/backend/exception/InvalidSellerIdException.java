package com.incon.backend.exception;

public class InvalidSellerIdException extends RuntimeException {
    public InvalidSellerIdException(String message) {
        super(message);
    }
}
