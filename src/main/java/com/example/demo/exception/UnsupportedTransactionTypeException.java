package com.example.demo.exception;

import com.example.demo.model.TransactionType;

public class UnsupportedTransactionTypeException extends Exception{
    public UnsupportedTransactionTypeException(TransactionType type) {
        super(String.format("Operation support for %s is not implemented", type));
    }
}
