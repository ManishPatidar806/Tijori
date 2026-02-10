package com.financialapplication.tijori.Service;

import com.financialapplication.tijori.Config.CacheConfig;
import com.financialapplication.tijori.Exception.BusinessException;
import com.financialapplication.tijori.Exception.NotFoundException;
import com.financialapplication.tijori.Model.Entity.CategoryBudget;
import com.financialapplication.tijori.Model.Entity.User;
import com.financialapplication.tijori.Repository.CategoryBudgetRepository;
import com.financialapplication.tijori.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryBudgetServiceImpl implements CategoryBudgetService {

    private final CategoryBudgetRepository categoryBudgetRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = CacheConfig.CATEGORY_CACHE, key = "#mobileNo")
    public List<CategoryBudget> getCategoryBudgetsByMobileNo(String mobileNo) {
        log.debug("Fetching category budgets for mobile: {}", mobileNo);

        if (mobileNo == null || mobileNo.trim().isEmpty()) {
            log.warn("Invalid mobile number provided.");
            throw new BusinessException("Mobile number cannot be null or empty.");
        }

        User user = userRepository.findByMobile(mobileNo)
                .orElseThrow(() -> new NotFoundException("User not found with mobile: " + mobileNo));

        List<CategoryBudget> categoryBudgets = categoryBudgetRepository.findAllByUser_Id(user.getId())
                .orElse(List.of());

        log.info("Found {} category budgets for mobile number: {}", categoryBudgets.size(), mobileNo);
        return categoryBudgets;
    }
}