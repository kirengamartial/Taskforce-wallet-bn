package com.wallet.wallet.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class TransactionDTO {
    private Long id;
    private BigDecimal amount;
    private String description;
    private LocalDateTime dateTime;
    private String type;
    private Long accountId;
    private Long categoryId;
}
