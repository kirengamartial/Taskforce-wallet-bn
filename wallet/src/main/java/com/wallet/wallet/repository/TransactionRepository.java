package com.wallet.wallet.repository;

import com.wallet.wallet.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDateTime;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByAccountIdAndDateTimeBetween(
            Long accountId, LocalDateTime startDate, LocalDateTime endDate);

    List<Transaction> findByAccountIdAndCategoryId(Long accountId, Long categoryId);
}
