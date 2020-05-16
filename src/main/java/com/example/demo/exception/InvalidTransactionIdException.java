package com.example.demo.exception;

public class InvalidTransactionIdException extends Exception {
    public InvalidTransactionIdException(String id) {
        super(String.format("Given transaction id: %s is invalid", id));
    }
}
