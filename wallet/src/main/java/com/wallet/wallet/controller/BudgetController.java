package com.wallet.wallet.controller;

import com.wallet.wallet.dto.BudgetDTO;
import com.wallet.wallet.service.BudgetService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;
import java.util.List;

@RestController
@RequestMapping("/api/budgets")
@RequiredArgsConstructor
public class BudgetController {
    private final BudgetService budgetService;

    @PostMapping
    public ResponseEntity<BudgetDTO> createBudget(@RequestBody BudgetDTO budgetDTO) {
        return ResponseEntity.ok(budgetService.createBudget(budgetDTO));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<BudgetDTO>> getUserBudgets(@PathVariable Long userId) {
        return ResponseEntity.ok(budgetService.getUserBudgets(userId));
    }
}