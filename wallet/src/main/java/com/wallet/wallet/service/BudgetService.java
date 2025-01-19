package com.wallet.wallet.service;

import com.wallet.wallet.dto.BudgetDTO;
import com.wallet.wallet.entity.Budget;
import com.wallet.wallet.entity.Transaction;
import com.wallet.wallet.entity.TransactionType;
import com.wallet.wallet.exception.BudgetExceededException;
import com.wallet.wallet.exception.ResourceNotFoundException;
import com.wallet.wallet.repository.BudgetRepository;
import com.wallet.wallet.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class BudgetService {
    private final BudgetRepository budgetRepository;
    private final UserRepository userRepository;
    private final NotificationService notificationService;

    public BudgetDTO createBudget(BudgetDTO budgetDTO) {
        // Check if a budget already exists for this user
        boolean budgetExists = !budgetRepository.findByUserId(budgetDTO.getUserId()).isEmpty();

        if (budgetExists) {
            throw new IllegalStateException(
                    String.format("A budget already exists for user ID %d. Each user can only have one budget.",
                            budgetDTO.getUserId()));
        }

        // Create a new budget
        Budget budget = new Budget();
        budget.setLimit(budgetDTO.getLimit());
        budget.setCurrentAmount(BigDecimal.ZERO);

        budget.setUser(userRepository.findById(budgetDTO.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found")));

        budget = budgetRepository.save(budget);
        return convertToDTO(budget);
    }


    public void checkAndUpdateBudget(Transaction transaction) {
        // Only process expense transactions
        if (transaction.getType() != TransactionType.EXPENSE) {
            return;
        }

        // Get all budgets for the user
        List<Budget> budgets = budgetRepository.findByUserId(transaction.getAccount().getUser().getId());

        // If no budgets exist, skip the update
        if (budgets.isEmpty()) {
            return;
        }

        // Assuming only one budget exists per user
        Budget budget = budgets.get(0);
        Long userId = transaction.getAccount().getUser().getId();
        BigDecimal newAmount = budget.getCurrentAmount().add(transaction.getAmount());

        // Check if the budget limit is exceeded
        if (newAmount.compareTo(budget.getLimit()) > 0) {
            BigDecimal exceededBy = newAmount.subtract(budget.getLimit());

            // Create the notification message
            String notificationMessage = String.format(
                    "Budget exceeded by %s! Budget limit: %s, Total spent: %s",
                    exceededBy,
                    budget.getLimit(),
                    newAmount
            );


            // Update the budget amount even if it exceeds the limit
            budget.setCurrentAmount(newAmount);
            budgetRepository.save(budget);

            // Throw the exception after saving notification and updating budget
            throw new BudgetExceededException(notificationMessage);
        }

        // Update the current amount and save the budget
        budget.setCurrentAmount(newAmount);
        budgetRepository.save(budget);
    }



    public List<BudgetDTO> getUserBudgets(Long userId) {
        return budgetRepository.findByUserId(userId)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private BudgetDTO convertToDTO(Budget budget) {
        BudgetDTO dto = new BudgetDTO();
        dto.setId(budget.getId());
        dto.setLimit(budget.getLimit());
        dto.setCurrentAmount(budget.getCurrentAmount());
        dto.setUserId(budget.getUser().getId());
        return dto;
    }
}
