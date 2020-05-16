package com.example.demo.service.impl;

import com.example.demo.dto.TransactionCommitRequest;
import com.example.demo.dto.TransactionResponse;
import com.example.demo.exception.*;
import com.example.demo.model.Transaction;
import com.example.demo.model.TransactionType;
import com.example.demo.model.User;
import com.example.demo.repository.TransactionRepository;
import com.example.demo.service.TransactionService;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.time.Instant;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

@Service
public class TransactionServiceImpl implements TransactionService {

    private static final MathContext MATH_CONTEXT = new MathContext(2, RoundingMode.HALF_EVEN);

    private final ReentrantLock lock = new ReentrantLock();

    private TransactionRepository transactionRepository;

    private UserService userService;

    @Autowired
    public TransactionServiceImpl(TransactionRepository transactionRepository, UserService userService) {
        this.transactionRepository = transactionRepository;
        this.userService = userService;
    }

    @Override
    public List<TransactionResponse> getTransactionList() {
        return transactionRepository.findAll()
                .stream()
                .map(this:: transactionToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public TransactionResponse getById(String id) throws TransactionNotFoundException, InvalidTransactionIdException {
        Long transactionId = getTransactionId(id);
        return transactionToResponse(transactionRepository
                .findById(transactionId)
                .orElseThrow(() -> new TransactionNotFoundException(transactionId)));
    }

    @Override
    @Transactional
    public TransactionResponse commitTransaction(TransactionCommitRequest request)
            throws UnsupportedTransactionTypeException, InvalidAmountException, LowBalanceException {

        lock.lock();
        try {
            User user = userService.getUser();
            TransactionType type = request.getType();
            BigDecimal amount = getAmount(request);

            if (type.equals(TransactionType.CREDIT)) {
                commitCredit(user, amount);
            } else if (type.equals(TransactionType.DEBIT)) {
                commitDebit(user, amount);
            } else {
                throw new UnsupportedTransactionTypeException(type);
            }

            user = userService.saveUser(user);
            Transaction transaction = new Transaction();
            transaction.setUser(user);
            transaction.setType(request.getType());
            transaction.setAmount(request.getAmount());
            transaction.setEffectiveDate(Instant.now());

            return transactionToResponse(transactionRepository.save(transaction));
        } finally {
            lock.unlock();
        }
    }

    private void commitCredit(User user, BigDecimal amount) throws LowBalanceException {
        if(user.getBalance().compareTo(amount) < 0) {
            throw new LowBalanceException(amount);
        }
        user.setBalance(user.getBalance().subtract(amount, MATH_CONTEXT));
    }

    private void commitDebit(User user, BigDecimal amount) {
        user.setBalance(user.getBalance().add(amount, MATH_CONTEXT));
    }

    private TransactionResponse transactionToResponse(Transaction transaction) {
        return TransactionResponse.builder()
                .id(transaction.getId().toString())
                .type(transaction.getType())
                .amount(transaction.getAmount())
                .effectiveDate(transaction.getEffectiveDate())
                .build();
    }

    private Long getTransactionId(String id) throws InvalidTransactionIdException {
        try {
            return Long.parseLong(id);
        } catch (NumberFormatException ex) {
            ex.printStackTrace();
            throw new InvalidTransactionIdException(id);
        }
    }

    private BigDecimal getAmount(TransactionCommitRequest request) throws InvalidAmountException {
        BigDecimal amount = request.getAmount();
        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidAmountException(amount);
        }

        return amount;
    }
}
