package com.financialapplication.tijori.Model.Response;

import com.financialapplication.tijori.Model.Entity.CategoryBudget;
import lombok.Data;

import java.util.List;

@Data
public class CategoryBudgetResponse {

    private List<CategoryBudget> categoryBudgets;
    private String message;
    private Boolean status;

    public CategoryBudgetResponse() {
    }

    public CategoryBudgetResponse(List<CategoryBudget> categoryBudgets, String message, boolean status) {
        this.categoryBudgets = categoryBudgets;
        this.message = message;
        this.status = status;
    }
}
