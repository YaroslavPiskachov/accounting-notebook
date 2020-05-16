package com.example.demo.dto;

import com.example.demo.model.TransactionType;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@Builder
public class TransactionResponse {
    private String id;
    private TransactionType type;
    private BigDecimal amount;
    private Instant effectiveDate;
}
