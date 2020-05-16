package com.example.demo.exception;

public class TransactionNotFoundException extends Exception {
    public TransactionNotFoundException(Long id) {
        super(String.format("Transaction with given id: %s was not found.", id));
    }
}
