package com.wallet.wallet.service;

import com.wallet.wallet.dto.TransactionDTO;
import com.wallet.wallet.entity.Transaction;
import com.wallet.wallet.entity.TransactionType;
import com.wallet.wallet.exception.ResourceNotFoundException;
import com.wallet.wallet.repository.AccountRepository;
import com.wallet.wallet.repository.CategoryRepository;
import com.wallet.wallet.repository.TransactionRepository;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

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

    public TransactionDTO createTransaction(TransactionDTO transactionDTO) {
        Transaction transaction = new Transaction();
        transaction.setAmount(transactionDTO.getAmount());
        transaction.setDescription(transactionDTO.getDescription());
        transaction.setDateTime(transactionDTO.getDateTime());
        transaction.setType(TransactionType.valueOf(transactionDTO.getType()));

        transaction.setAccount(accountRepository.findById(transactionDTO.getAccountId())
                .orElseThrow(() -> new ResourceNotFoundException("Account not found")));

        transaction.setCategory(categoryRepository.findById(transactionDTO.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found")));

        // Check budget before saving
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
