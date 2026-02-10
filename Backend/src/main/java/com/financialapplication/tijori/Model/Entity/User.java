package com.financialapplication.tijori.Model.Entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * User entity representing a registered user in the system.
 * Contains personal information and relationships to transactions and expense reports.
 */
@Data
@Entity
@Table(name = "users", indexes = {
        @Index(name = "idx_user_mobile", columnList = "mobile"),
        @Index(name = "idx_user_email", columnList = "email"),
        @Index(name = "idx_user_created_at", columnList = "created_at")
})
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(nullable = false)
    private String name;
    
    @Column(nullable = false, unique = true)
    private String email;
    
    @Column(nullable = false)
    private String bank;
    
    @Column(nullable = false, unique = true, length = 15)
    private String mobile;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @JsonManagedReference
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Transaction> transactions;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<MonthlyExpenseReport> monthlyExpenseReports;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
