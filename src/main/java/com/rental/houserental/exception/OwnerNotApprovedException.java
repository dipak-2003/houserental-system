package com.rental.houserental.exception;

public class OwnerNotApprovedException extends RuntimeException {

    public OwnerNotApprovedException(String message) {
        super(message);
    }
}
