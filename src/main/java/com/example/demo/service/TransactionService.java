package com.example.demo.service;

import com.example.demo.dto.TransactionCommitRequest;
import com.example.demo.dto.TransactionResponse;
import com.example.demo.exception.*;

import java.util.List;

public interface TransactionService {
    List<TransactionResponse> getTransactionList();
    TransactionResponse getById(String id) throws TransactionNotFoundException, InvalidTransactionIdException;
    TransactionResponse commitTransaction(TransactionCommitRequest request) throws UnsupportedTransactionTypeException, InvalidAmountException, LowBalanceException;
}
