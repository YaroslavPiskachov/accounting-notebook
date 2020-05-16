package com.example.demo.dto;

import com.example.demo.model.TransactionType;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class TransactionCommitRequest {
    private TransactionType type;
    private BigDecimal amount;
}
