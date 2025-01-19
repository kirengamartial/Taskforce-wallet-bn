package com.wallet.wallet.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;

@Entity
@Data
@Table(name = "budgets", uniqueConstraints = @UniqueConstraint(columnNames = {"user_id"}))
public class Budget {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "budget_limit")
    private BigDecimal limit;

    private BigDecimal currentAmount;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
