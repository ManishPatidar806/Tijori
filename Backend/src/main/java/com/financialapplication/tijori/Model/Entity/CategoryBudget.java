package com.financialapplication.tijori.Model.Entity;

import com.financialapplication.tijori.Model.Enum.CategoryType;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

/**
 * CategoryBudget entity for tracking expense amounts by category per user.
 * Represents spending breakdown across different expense categories.
 */
@Data
@Entity
@DynamicInsert
@Table(name = "category_budget", indexes = {
        @Index(name = "idx_category_budget_user_id", columnList = "user_id"),
        @Index(name = "idx_category_budget_type", columnList = "category_type")
})
public class CategoryBudget {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "category_type")
    private CategoryType categoryType;

    @ColumnDefault("0")
    @Column(name = "total_amount")
    private Double totalAmount;


    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private User user;

}
