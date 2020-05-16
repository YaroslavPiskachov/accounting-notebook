package com.example.demo.exception;

import java.math.BigDecimal;

public class LowBalanceException extends Exception {
    public LowBalanceException(BigDecimal amount) {
        super(String.format("Not enough balance to credit: %s", amount));
    }
}
