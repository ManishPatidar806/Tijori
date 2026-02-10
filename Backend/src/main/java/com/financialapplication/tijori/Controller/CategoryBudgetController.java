package com.financialapplication.tijori.Controller;

import com.financialapplication.tijori.Model.Entity.CategoryBudget;
import com.financialapplication.tijori.Model.Response.ApiResponse;
import com.financialapplication.tijori.Service.CategoryBudgetService;
import io.micrometer.core.annotation.Timed;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/v1/api/category-budgets")
@RequiredArgsConstructor
@Tag(name = "Category Budget Management", description = "Expense category budget breakdown APIs")
public class CategoryBudgetController {

    private final CategoryBudgetService categoryBudgetService;

    @Operation(summary = "Get category budget amounts",
               description = "Retrieves expense amounts grouped by category for graph visualization")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Category budgets retrieved successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "User or category data not found")
    })
    @GetMapping("/amounts")
    @Timed(value = "category.budget.amount.get.time", description = "Time taken to get category budget amounts")
    public ResponseEntity<ApiResponse<List<CategoryBudget>>> getCategoryBudgetAmounts(
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        log.debug("Fetching category budgets for user: {}", userDetails.getUsername());

        List<CategoryBudget> categoryBudgets = categoryBudgetService.getCategoryBudgetsByMobileNo(userDetails.getUsername());

        return ResponseEntity.ok(ApiResponse.success("Category budgets retrieved successfully.", categoryBudgets));
    }
}
