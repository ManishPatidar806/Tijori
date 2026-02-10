package com.financialapplication.tijori.Controller;

import com.financialapplication.tijori.Exception.NotFoundException;
import com.financialapplication.tijori.Model.Response.ApiResponse;
import com.financialapplication.tijori.Service.AccountBalanceService;
import io.micrometer.core.annotation.Timed;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Validated
@RestController
@RequestMapping("/v1/api/account-balance")
@RequiredArgsConstructor
@Tag(name = "Account Balance Management", description = "Income and budget management APIs")
public class AccountBalanceController {

    private final AccountBalanceService accountBalanceService;

    @Operation(summary = "Set income and savings",
               description = "Sets the user's monthly income and savings amount to calculate budget limit")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Income and savings set successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid input - income must be greater than savings"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "User not found")
    })
    @PostMapping("/income")
    @Timed(value = "account.balance.income.set.time", description = "Time taken to set income")
    public ResponseEntity<ApiResponse<Void>> createExpenseLimit(
            @AuthenticationPrincipal UserDetails userDetails,
            @Parameter(description = "Monthly savings amount")
            @RequestParam @NotNull(message = "Saving is required") @Positive(message = "Saving must be positive") Double saving,
            @Parameter(description = "Monthly income amount")
            @RequestParam @NotNull(message = "Income is required") @Positive(message = "Income must be positive") Double income
    ) throws NotFoundException {
        log.info("Setting income for user: {}, income: {}, saving: {}",
                userDetails.getUsername(), income, saving);
        accountBalanceService.saveIncome(userDetails.getUsername(), saving, income);
        return ResponseEntity.ok(ApiResponse.success("Income and savings set successfully."));
    }
}
