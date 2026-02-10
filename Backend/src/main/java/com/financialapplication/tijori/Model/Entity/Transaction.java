package com.financialapplication.tijori.Model.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.financialapplication.tijori.Model.Enum.CategoryType;
import com.financialapplication.tijori.Model.Enum.TransactionType;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * Transaction entity representing financial transactions extracted from SMS messages.
 * Stores transaction details including amount, category, and timestamp.
 */
@Data
@Entity
@Table(name = "transaction", indexes = {
        @Index(name = "idx_transaction_user_id", columnList = "user_id"),
        @Index(name = "idx_transaction_category", columnList = "category"),
        @Index(name = "idx_transaction_date_time", columnList = "date_time"),
        @Index(name = "idx_transaction_user_category", columnList = "user_id, category"),
        @Index(name = "idx_transaction_type", columnList = "transaction_type")
})
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private double amount;
    
    @Column(name = "reference_number")
    private String referenceNumber;

    @Enumerated(EnumType.STRING)
    @Column(length = 50)
    private CategoryType category;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "transaction_type", length = 20)
    private TransactionType transactionType;

    @Column(name = "date_time")
    private LocalDateTime dateTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonBackReference
    private User user;
}
