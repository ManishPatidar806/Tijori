package com.financialapplication.tijori.Service;

import com.financialapplication.tijori.Exception.BusinessException;
import com.financialapplication.tijori.Exception.NotFoundException;
import com.financialapplication.tijori.Model.Entity.AccountBalance;
import com.financialapplication.tijori.Model.Entity.User;
import com.financialapplication.tijori.Repository.AccountBalanceRepository;
import com.financialapplication.tijori.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.financialapplication.tijori.Config.CacheConfig.USER_PROFILE_CACHE;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountBalanceServiceImpl implements AccountBalanceService {

    private final AccountBalanceRepository accountBalanceRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    @CacheEvict(value = USER_PROFILE_CACHE, key = "#mobileNo")
    public void saveIncome(String mobileNo, double saving, double income) throws NotFoundException {
        log.info("Saving income for mobile number: {}", mobileNo);

        if (mobileNo == null || mobileNo.trim().isEmpty()) {
            log.warn("Invalid mobile number provided.");
            throw new BusinessException("Mobile number cannot be null or empty.");
        }
        if (saving < 0 || income < 0) {
            log.warn("Invalid saving or income values provided.");
            throw new BusinessException("Saving and income must be positive values.");
        }
        if (income <= saving) {
            log.warn("Income is less than or equal to saving.");
            throw new BusinessException("Income must be greater than saving.");
        }

        User user = userRepository.findByMobile(mobileNo)
                .orElseThrow(() -> new NotFoundException("User not found for mobile number: " + mobileNo));

        AccountBalance accountBalance = accountBalanceRepository.findByUser_Id(user.getId())
                .orElseThrow(() -> new NotFoundException("Account balance details not found for mobile number: " + mobileNo));

        double limit = income - saving;
        accountBalance.setIncome(income);
        accountBalance.setTotalSavings(saving);
        accountBalance.setMonthlyLimit(limit);
        accountBalance.setBudgetLimit(limit);
        accountBalanceRepository.save(accountBalance);

        log.info("Income and saving updated successfully for mobile number: {}. Monthly limit: {}", mobileNo, limit);
    }
}