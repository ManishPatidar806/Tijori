package com.financialapplication.tijori.Repository;


import com.financialapplication.tijori.Model.Entity.AccountBalance;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface AccountBalanceRepository extends JpaRepository<AccountBalance, Long> {

    Optional<AccountBalance> findAccountBalanceByUser_Id(Long userId);

    Optional<AccountBalance> findByUser_Id(Long userId);


    @Query(value = " SELECT * FROM account_balance WHERE DATE(start_date) = CURRENT_DATE - INTERVAL 1 MONTH", nativeQuery = true)
    List<AccountBalance> findExactlyOneMonthOldEntries();




}
