package com.financialapplication.tijori.Controller;

import com.financialapplication.tijori.Exception.NotFoundException;
import com.financialapplication.tijori.Model.Entity.Transaction;
import com.financialapplication.tijori.Model.Enum.CategoryType;
import com.financialapplication.tijori.Model.Request.TransactionSaveRequest;
import com.financialapplication.tijori.Model.Request.TransactionUpdateRequest;
import com.financialapplication.tijori.Model.Response.ApiResponse;
import com.financialapplication.tijori.Service.TransactionService;
import io.micrometer.core.annotation.Timed;
import io.micrometer.core.instrument.Counter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Validated
@RestController
@RequestMapping("/v1/api/transactions")
@RequiredArgsConstructor
@Tag(name = "Transactions", description = "Financial transaction tracking and categorization APIs")
public class TransactionController {

    private final TransactionService transactionService;
    private final Counter transactionCounter;
    private final Counter categorizationCounter;

    @Operation(summary = "Save transactions", description = "Saves the latest transactions as expense entries")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Transactions saved successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "204", description = "No new transactions to save"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "User not found")
    })
    @PostMapping("/save")
    @Timed(value = "transaction.save.time", description = "Time taken to save transactions")
    public ResponseEntity<ApiResponse<Integer>> saveTransactions(
            @RequestBody @Valid TransactionSaveRequest transactionRequest,
            @AuthenticationPrincipal UserDetails userDetails
    ) throws NotFoundException {
        log.info("Processing transaction save request for user: {}", userDetails.getUsername());

        int savedCount = transactionService.saveTransactions(transactionRequest, userDetails.getUsername());

        if (savedCount == 0) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT)
                    .body(ApiResponse.success("No new transactions to save.", savedCount));
        }

        transactionCounter.increment(savedCount);
        return ResponseEntity.ok(ApiResponse.success("Transactions saved successfully. " + savedCount + " records processed.", savedCount));
    }

    @Operation(summary = "Get uncategorized transactions", description = "Retrieves all transactions that haven't been categorized yet")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Transaction list retrieved successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "204", description = "No uncategorized transactions found"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "User not found")
    })
    @GetMapping("/categorization")
    @Timed(value = "transaction.categorization.get.time", description = "Time taken to get uncategorized transactions")
    public ResponseEntity<ApiResponse<List<Transaction>>> getTransactionsForCategorization(
            @AuthenticationPrincipal UserDetails userDetails
    ) throws NotFoundException {
        log.debug("Fetching uncategorized transactions for user: {}", userDetails.getUsername());

        List<Transaction> transactionList = transactionService.getAllUncategorizedTransactions(userDetails.getUsername());

        if (transactionList.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT)
                    .body(ApiResponse.success("No uncategorized transactions found.", transactionList));
        }

        return ResponseEntity.ok(ApiResponse.success("Transactions retrieved successfully.", transactionList));
    }

    @Operation(summary = "Get transactions by category", description = "Retrieves all transactions filtered by the specified category")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Transaction list retrieved successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "204", description = "No transactions found for category"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "User not found")
    })
    @GetMapping("/category")
    @Timed(value = "transaction.category.get.time", description = "Time taken to get transactions by category")
    public ResponseEntity<ApiResponse<List<Transaction>>> getTransactionsByCategory(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam @NotNull CategoryType category
    ) throws NotFoundException {
        log.debug("Fetching transactions for category {} for user: {}", category, userDetails.getUsername());

        List<Transaction> transactionList = transactionService.getTransactionsByCategory(userDetails.getUsername(), category);

        if (transactionList.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT)
                    .body(ApiResponse.success("No transactions found for the selected category.", transactionList));
        }

        return ResponseEntity.ok(ApiResponse.success("Transactions found successfully for category: " + category, transactionList));
    }

    @Operation(summary = "Categorize transactions", description = "Updates the category for transaction entries")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Categories updated successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "User not found")
    })
    @PostMapping("/categorization")
    @Timed(value = "transaction.categorization.update.time", description = "Time taken to update categories")
    public ResponseEntity<ApiResponse<Integer>> categorizeTransactions(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody @Valid TransactionUpdateRequest transactionRequest
    ) throws NotFoundException {
        log.info("Processing categorization request for user: {}", userDetails.getUsername());

        int updatedCount = transactionService.updateCategory(userDetails.getUsername(), transactionRequest);

        categorizationCounter.increment(updatedCount);
        return ResponseEntity.ok(ApiResponse.success("Categories updated successfully. " + updatedCount + " records processed.", updatedCount));
    }
}
