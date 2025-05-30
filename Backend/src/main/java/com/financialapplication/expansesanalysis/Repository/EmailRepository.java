package com.financialapplication.expansesanalysis.Repository;

import com.financialapplication.expansesanalysis.Model.Entity.OTP;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;

public interface EmailRepository extends JpaRepository<OTP, Long> {
    OTP findByEmail(String email);

    @Modifying
    @Transactional
    @Query("DELETE FROM OTP o WHERE o.expiryTime<:now")
    void deleteByExpiryTimeBefore(@Param("now") LocalDateTime now);
}
