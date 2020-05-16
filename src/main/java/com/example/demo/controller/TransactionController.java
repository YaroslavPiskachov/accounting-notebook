package com.example.demo.controller;

import com.example.demo.dto.TransactionCommitRequest;
import com.example.demo.dto.TransactionResponse;
import com.example.demo.exception.*;
import com.example.demo.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class TransactionController {

    private TransactionService transactionService;

    @Autowired
    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @GetMapping("/transaction")
    public List<TransactionResponse> getTransactionList() {
        return transactionService.getTransactionList();
    }

    @GetMapping("/transaction/{id}")
    public TransactionResponse getTransactionById(@PathVariable String id)
            throws TransactionNotFoundException, InvalidTransactionIdException {
        return transactionService.getById(id);
    }

    @PostMapping("/transaction/commit")
    public TransactionResponse commitTransaction(@RequestBody TransactionCommitRequest request)
            throws UnsupportedTransactionTypeException, InvalidAmountException, LowBalanceException {
        return transactionService.commitTransaction(request);
    }




    @ExceptionHandler(value = TransactionNotFoundException.class)
    public ResponseEntity notFoundException(TransactionNotFoundException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({InvalidTransactionIdException.class, InvalidAmountException.class, LowBalanceException.class})
    public ResponseEntity validationException(Exception exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity generalException(Exception exception) {
        return new ResponseEntity<>("Some error occurred", HttpStatus.BAD_REQUEST);
    }

}
