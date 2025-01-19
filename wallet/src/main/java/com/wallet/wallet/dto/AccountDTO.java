package com.wallet.wallet.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class AccountDTO {
    private Long id;
    private String name;
    private String type;
    private BigDecimal balance;
    private Long userId;
}
