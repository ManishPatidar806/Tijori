package com.financialapplication.tijori.Repository;

import com.financialapplication.tijori.Model.Entity.Transaction;
import com.financialapplication.tijori.Model.Enum.CategoryType;
import com.financialapplication.tijori.Model.Enum.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    /**
     * Finds the latest transaction date/time for a user.
     *
     * @param userId the user ID
     * @return the latest transaction date/time
     */
    @Query("SELECT MAX(t.dateTime) FROM Transaction t WHERE t.user.id = :userId")
    LocalDateTime findLatestDateTime(@Param("userId") Long userId);

    List<Transaction> findAllByUser_IdAndTransactionTypeAndCategoryIsNull(
            Long userId,
            TransactionType transactionType
    );

    List<Transaction> findAllByUser_IdAndCategory(Long userId, CategoryType category);
}
