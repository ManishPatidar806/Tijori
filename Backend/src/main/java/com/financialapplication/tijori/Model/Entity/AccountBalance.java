package com.financialapplication.tijori.Model.Entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * AccountBalance entity for tracking user's financial data including income, expenses, and budgets.
 * Represents the current financial summary for a user.
 */
@Data
@Entity
@Table(name = "account_balance", indexes = {
        @Index(name = "idx_account_balance_user_id", columnList = "user_id"),
        @Index(name = "idx_account_balance_start_date", columnList = "start_date")
})
public class AccountBalance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "total_credits")
    private Double totalCredits = 0.0;

    @Column(name = "total_debits")
    private Double totalDebits = 0.0;

    @Column(name = "total_savings")
    private Double totalSavings = 0.0;

    private Double income = 0.0;

    @Column(name = "monthly_limit")
    private Double monthlyLimit = 0.0;

    @Column(name = "budget_limit")
    private Double budgetLimit = 0.0;

    private LocalDateTime updatedAt;
    private LocalDateTime startDate;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private User user;
}
