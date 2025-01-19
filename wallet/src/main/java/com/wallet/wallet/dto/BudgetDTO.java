package com.wallet.wallet.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class BudgetDTO {
    private Long id;
    private BigDecimal limit;
    private BigDecimal currentAmount;
    private Long userId;
}
