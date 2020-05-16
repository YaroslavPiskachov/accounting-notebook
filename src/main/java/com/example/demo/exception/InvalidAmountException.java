package com.example.demo.exception;

import java.math.BigDecimal;

public class InvalidAmountException extends Exception {
    public InvalidAmountException(BigDecimal amount) {
        super(String.format("Given transaction amount: %s could not be processed", amount));
    }
}
