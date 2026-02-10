package com.financialapplication.tijori.Repository;

import com.financialapplication.tijori.Model.Entity.CategoryBudget;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryBudgetRepository extends JpaRepository<CategoryBudget, Long> {

    @Modifying
    @Transactional
    @Query(value = """
            UPDATE category_budget SET total_amount = 0 WHERE user_id IN (SELECT user_id
            FROM users u
            WHERE DATE(u.created_at) = CURRENT_DATE - INTERVAL 1 MONTH)""", nativeQuery = true)
    void updatePreviousCategoryData();
    
    Optional<List<CategoryBudget>> findAllByUser_Id(Long userId);
}
