package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.math.BigDecimal;

public enum TransactionType {
    DEBIT("debit"), CREDIT("credit");

    private String value;

    TransactionType(String value) {
        this.value = value;
    }

    @Override
    @JsonValue
    public String toString() {
        return String.valueOf(value);
    }

    @JsonCreator
    public static TransactionType fromValue(String text) {
        for (TransactionType type : TransactionType.values()) {
            if (String.valueOf(type.value).equals(text)) {
                return type;
            }
        }
        return null;
    }
}
