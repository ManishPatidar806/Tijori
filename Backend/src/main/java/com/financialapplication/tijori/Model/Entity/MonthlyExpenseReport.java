package com.financialapplication.tijori.Model.Entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.Month;

/**
 * MonthlyExpenseReport entity for storing monthly expense history records.
 * Captures financial summary at the end of each month for historical tracking.
 */
@Entity
@Data
@Table(name = "monthly_expense_report", indexes = {
        @Index(name = "idx_monthly_expense_report_user_id", columnList = "user_id"),
        @Index(name = "idx_monthly_expense_report_year_month", columnList = "year, month")
})
public class MonthlyExpenseReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime recordedAt;

    @Enumerated(EnumType.STRING)
    private Month month;

    private Integer year;

    @Column(name = "total_credits")
    private Double totalCredits;

    @Column(name = "total_debits")
    private Double totalDebits;

    @Column(name = "net_savings")
    private Double netSavings;

    private Double income;


    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
