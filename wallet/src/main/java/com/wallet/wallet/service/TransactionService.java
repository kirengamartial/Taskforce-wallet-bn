package com.wallet.wallet.service;

import com.wallet.wallet.dto.TransactionDTO;
import com.wallet.wallet.entity.Account;
import com.wallet.wallet.entity.Transaction;
import com.wallet.wallet.entity.TransactionType;
import com.wallet.wallet.exception.ResourceNotFoundException;
import com.wallet.wallet.repository.AccountRepository;
import com.wallet.wallet.repository.CategoryRepository;
import com.wallet.wallet.repository.TransactionRepository;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;
    private final CategoryRepository categoryRepository;
    private final BudgetService budgetService;

    @Transactional
    public TransactionDTO createTransaction(TransactionDTO transactionDTO) {

        Account account = accountRepository.findById(transactionDTO.getAccountId())
                .orElseThrow(() -> new ResourceNotFoundException("Account not found"));

        BigDecimal transactionAmount = transactionDTO.getAmount();
        if (TransactionType.valueOf(transactionDTO.getType()) == TransactionType.EXPENSE) {
            if (account.getBalance().compareTo(transactionAmount) < 0) {
                throw new IllegalArgumentException("Insufficient funds in the account for this transaction.");
            }

            account.setBalance(account.getBalance().subtract(transactionAmount));
        } else if (TransactionType.valueOf(transactionDTO.getType()) == TransactionType.INCOME) {

            account.setBalance(account.getBalance().add(transactionAmount));
        } else {
            throw new IllegalArgumentException("Invalid transaction type.");
        }


        accountRepository.save(account);

        Transaction transaction = new Transaction();
        transaction.setAmount(transactionAmount);
        transaction.setDescription(transactionDTO.getDescription());
        transaction.setDateTime(transactionDTO.getDateTime());
        transaction.setType(TransactionType.valueOf(transactionDTO.getType()));
        transaction.setAccount(account);
        transaction.setCategory(categoryRepository.findById(transactionDTO.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found")));

        budgetService.checkAndUpdateBudget(transaction);
        transaction = transactionRepository.save(transaction);

        return convertToDTO(transaction);
    }


    public List<TransactionDTO> getTransactionsByAccountAndPeriod(
            Long accountId, LocalDateTime startDate, LocalDateTime endDate) {
        return transactionRepository
                .findByAccountIdAndDateTimeBetween(accountId, startDate, endDate)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private TransactionDTO convertToDTO(Transaction transaction) {
        TransactionDTO dto = new TransactionDTO();
        dto.setId(transaction.getId());
        dto.setAmount(transaction.getAmount());
        dto.setDescription(transaction.getDescription());
        dto.setDateTime(transaction.getDateTime());
        dto.setType(transaction.getType().toString());
        dto.setAccountId(transaction.getAccount().getId());
        dto.setCategoryId(transaction.getCategory().getId());
        return dto;
    }
}
