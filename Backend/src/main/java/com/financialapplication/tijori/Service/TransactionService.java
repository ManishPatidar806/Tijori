package com.financialapplication.tijori.Service;

import com.financialapplication.tijori.Exception.NotFoundException;
import com.financialapplication.tijori.Model.Entity.Transaction;
import com.financialapplication.tijori.Model.Enum.CategoryType;
import com.financialapplication.tijori.Model.Request.TransactionSaveRequest;
import com.financialapplication.tijori.Model.Request.TransactionUpdateRequest;

import java.util.List;

/**
 * Service interface for financial transaction management.
 */
public interface TransactionService {

    /**
     * Saves transactions for a user.
     * @param transactionRequest The transaction data to save
     * @param mobileNo The user's mobile number
     * @return Number of transaction records saved
     */
    int saveTransactions(TransactionSaveRequest transactionRequest, String mobileNo) throws NotFoundException;

    /**
     * Gets all uncategorized transactions for a user.
     * @param mobileNo The user's mobile number
     * @return List of uncategorized transactions
     */
    List<Transaction> getAllUncategorizedTransactions(String mobileNo) throws NotFoundException;

    /**
     * Gets transactions by category for a user.
     * @param mobileNo The user's mobile number
     * @param category The category to filter by
     * @return List of transactions matching the category
     */
    List<Transaction> getTransactionsByCategory(String mobileNo, CategoryType category) throws NotFoundException;

    /**
     * Updates categories for transactions.
     * @param mobileNo The user's mobile number
     * @param transactionRequest The categorization data
     * @return Number of transaction records updated
     */
    int updateCategory(String mobileNo, TransactionUpdateRequest transactionRequest) throws NotFoundException;
}
