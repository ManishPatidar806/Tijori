package com.financialapplication.tijori.Service;

import com.financialapplication.tijori.Model.Entity.CategoryBudget;

import java.util.List;

/**
 * Service interface for expense category budget management.
 */
public interface CategoryBudgetService {

    /**
     * Gets category-wise expense amounts for a user.
     * @param mobileNo The user's mobile number
     * @return List of CategoryBudget entities with expense breakdowns
     */
    List<CategoryBudget> getCategoryBudgetsByMobileNo(String mobileNo);
}
