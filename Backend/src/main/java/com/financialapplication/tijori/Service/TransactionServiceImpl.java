package com.financialapplication.tijori.Service;

import com.financialapplication.tijori.Config.CacheConfig;
import com.financialapplication.tijori.Exception.NotFoundException;
import com.financialapplication.tijori.Model.DTO.UsageNotificationRequest;
import com.financialapplication.tijori.Model.Entity.CategoryBudget;
import com.financialapplication.tijori.Model.Entity.AccountBalance;
import com.financialapplication.tijori.Model.Entity.Transaction;
import com.financialapplication.tijori.Model.Entity.User;
import com.financialapplication.tijori.Model.Enum.CategoryType;
import com.financialapplication.tijori.Model.Enum.TransactionType;
import com.financialapplication.tijori.Model.Request.TransactionSaveRequest;
import com.financialapplication.tijori.Model.Request.TransactionUpdateRequest;
import com.financialapplication.tijori.Repository.CategoryBudgetRepository;
import com.financialapplication.tijori.Repository.AccountBalanceRepository;
import com.financialapplication.tijori.Repository.TransactionRepository;
import com.financialapplication.tijori.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
/**
 * Service implementation for financial transaction management.
 * Implements caching for frequently accessed data and proper transaction management.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final ModelMapper mapper;
    private final UserRepository userRepository;
    private final AccountBalanceRepository accountBalanceRepository;
    private final TransactionRepository transactionRepository;
    private final CategoryBudgetRepository categoryBudgetRepository;
    private final EmailService emailService;

    @Override
    @Transactional
    @CacheEvict(value = {CacheConfig.SMS_BY_CATEGORY_CACHE, CacheConfig.USER_PROFILE_CACHE},
                key = "#mobileNo", allEntries = true)
    public int saveTransactions(TransactionSaveRequest transactionRequest, String mobileNo) throws NotFoundException {
        log.info("Saving transactions for mobile number: {}", mobileNo);

        if (transactionRequest.getTransactions().isEmpty()) {
            log.debug("No transactions to save for mobile: {}", mobileNo);
            return 0;
        }

        User user = fetchUserByMobile(mobileNo);
        LocalDateTime latestTime = transactionRepository.findLatestDateTime(user.getId());
        LocalDateTime oneMonthBeforeUserCreated = user.getCreatedAt().minusMonths(1);

        List<Transaction> filteredTransactionList = transactionRequest.getTransactions().stream()
                .map(txnDto -> mapper.map(txnDto, Transaction.class))
                .filter(txn -> isTransactionValid(txn, latestTime, oneMonthBeforeUserCreated))
                .peek(txn -> txn.setUser(user))
                .toList();

        if (filteredTransactionList.isEmpty()) {
            log.debug("No new transactions to save after filtering for mobile: {}", mobileNo);
            return 0;
        }

        Map<Boolean, List<Transaction>> partitioned = partitionTransactionsByType(filteredTransactionList);

        double credited = calculateTotalAmount(partitioned.get(true));
        double debited = calculateTotalAmount(partitioned.get(false));
        double expenses = calculateExpenses(filteredTransactionList, user.getCreatedAt(), latestTime);

        AccountBalance accountBalance = accountBalanceRepository.findAccountBalanceByUser_Id(user.getId())
                .orElseGet(() -> createNewAccountBalance(user));
        updateAccountBalance(accountBalance, user.getName(), credited, debited, expenses, user.getEmail());

        transactionRepository.saveAll(filteredTransactionList);
        accountBalanceRepository.save(accountBalance);

        log.info("Saved {} transaction records for mobile: {}", filteredTransactionList.size(), mobileNo);
        return filteredTransactionList.size();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Transaction> getAllUncategorizedTransactions(String mobileNo) throws NotFoundException {
        log.debug("Fetching uncategorized transactions for mobile: {}", mobileNo);
        User user = fetchUserByMobile(mobileNo);
        List<Transaction> transactionList = transactionRepository.findAllByUser_IdAndTransactionTypeAndCategoryIsNull(
                user.getId(), TransactionType.DEBIT);
        log.debug("Found {} uncategorized transactions for mobile: {}", transactionList.size(), mobileNo);
        return transactionList;
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = CacheConfig.SMS_BY_CATEGORY_CACHE, key = "#mobileNo + '_' + #category")
    public List<Transaction> getTransactionsByCategory(String mobileNo, CategoryType category) throws NotFoundException {
        log.debug("Fetching transactions by category {} for mobile: {}", category, mobileNo);
        User user = fetchUserByMobile(mobileNo);
        List<Transaction> transactionList = transactionRepository.findAllByUser_IdAndCategory(user.getId(), category);
        log.debug("Found {} transactions for category {} and mobile: {}", transactionList.size(), category, mobileNo);
        return transactionList;
    }

    @Override
    @Transactional
    @CacheEvict(value = {CacheConfig.SMS_BY_CATEGORY_CACHE, CacheConfig.CATEGORY_CACHE},
                key = "#mobileNo", allEntries = true)
    public int updateCategory(String mobileNo, TransactionUpdateRequest transactionRequest) throws NotFoundException {
        log.info("Updating categories for mobile: {}", mobileNo);
        User user = fetchUserByMobile(mobileNo);

        List<Transaction> filteredTransactions = transactionRequest.getTransactions().stream()
                .map(txnDto -> mapper.map(txnDto, Transaction.class))
                .filter(txn -> txn.getCategory() != null)
                .peek(txn -> txn.setUser(user))
                .toList();

        // Update category budget amounts
        updateCategoryBudgetAmounts(user.getId(), filteredTransactions);

        transactionRepository.saveAll(filteredTransactions);

        log.info("Updated {} transaction categories for mobile: {}", filteredTransactions.size(), mobileNo);
        return filteredTransactions.size();
    }

    private User fetchUserByMobile(String mobileNo) throws NotFoundException {
        return userRepository.findByMobile(mobileNo)
                .orElseThrow(() -> new NotFoundException("User not found with mobile number: " + mobileNo));
    }

    private boolean isTransactionValid(Transaction txn, LocalDateTime latestTime, LocalDateTime oneMonthBeforeUserCreated) {
        LocalDateTime txnDateTime = txn.getDateTime();
        return txnDateTime != null &&
                (latestTime == null || txnDateTime.isAfter(latestTime)) &&
                txnDateTime.isAfter(oneMonthBeforeUserCreated);
    }

    private Map<Boolean, List<Transaction>> partitionTransactionsByType(List<Transaction> transactionList) {
        return transactionList.stream()
                .collect(Collectors.partitioningBy(txn -> txn.getTransactionType() == TransactionType.CREDIT));
    }

    private double calculateTotalAmount(List<Transaction> transactionList) {
        return transactionList.stream().mapToDouble(Transaction::getAmount).sum();
    }

    private double calculateExpenses(List<Transaction> transactionList, LocalDateTime userCreatedAt, LocalDateTime latestTime) {
        return transactionList.stream()
                .filter(txn -> {
                    LocalDateTime txnDateTime = txn.getDateTime();
                    return txnDateTime != null &&
                            txn.getTransactionType() == TransactionType.DEBIT &&
                            txnDateTime.isAfter(userCreatedAt) &&
                            (latestTime == null || txnDateTime.isAfter(latestTime));
                })
                .mapToDouble(Transaction::getAmount)
                .sum();
    }

    private AccountBalance createNewAccountBalance(User user) {
        AccountBalance accountBalance = new AccountBalance();
        accountBalance.setUser(user);
        return accountBalance;
    }

    private void updateAccountBalance(AccountBalance accountBalance, String name, double credited, double debited, double expenses, String email) {
        double currentCredit = accountBalance.getTotalCredits() != null ? accountBalance.getTotalCredits() : 0.0;
        double currentDebit = accountBalance.getTotalDebits() != null ? accountBalance.getTotalDebits() : 0.0;
        double currentLimit = accountBalance.getMonthlyLimit() != null ? accountBalance.getMonthlyLimit() : 0.0;
        double budgetLimit = accountBalance.getBudgetLimit() != null ? accountBalance.getBudgetLimit() : 0.0;

        accountBalance.setTotalCredits(currentCredit + credited);
        accountBalance.setTotalDebits(currentDebit + debited);
        double notificationLimit = currentLimit - expenses;
        double percentage = (budgetLimit * 10) / 100;

        if (budgetLimit > 0 && percentage >= notificationLimit) {
            sendLimitNotification(email, name, notificationLimit, budgetLimit);
        }

        accountBalance.setMonthlyLimit(currentLimit - expenses);
    }

    private void sendLimitNotification(String email, String name, double remainingAmount, Double budgetLimit) {
        try {
            log.info("Sending limit notification to {}: Remaining ₹{} of ₹{}",
                    email, remainingAmount, budgetLimit);

            double usedAmount = budgetLimit - remainingAmount;
            UsageNotificationRequest request = UsageNotificationRequest.builder()
                    .email(email)
                    .userName(name)
                    .usedAmount(java.math.BigDecimal.valueOf(usedAmount))
                    .totalAmount(java.math.BigDecimal.valueOf(budgetLimit))
                    .build();

            emailService.sendUsageNotificationEmailAsync(request);
        } catch (Exception e) {
            log.error("Failed to send limit notification: {}", e.getMessage());
        }
    }

    private void updateCategoryBudgetAmounts(Long userId, List<Transaction> filteredTransactions) {
        Map<CategoryType, Double> categoryAmounts = filteredTransactions.stream()
                .collect(Collectors.groupingBy(Transaction::getCategory, Collectors.summingDouble(Transaction::getAmount)));

        List<CategoryBudget> existingCategoryBudgets = categoryBudgetRepository.findAllByUser_Id(userId)
                .orElse(List.of());

        User user = userRepository.findById(userId).orElse(null);

        categoryAmounts.forEach((categoryType, amount) -> {
            CategoryBudget categoryBudget = existingCategoryBudgets.stream()
                    .filter(cb -> cb.getCategoryType() == categoryType)
                    .findFirst()
                    .orElseGet(() -> {
                        CategoryBudget newCategoryBudget = new CategoryBudget();
                        newCategoryBudget.setCategoryType(categoryType);
                        newCategoryBudget.setTotalAmount(0.0);
                        newCategoryBudget.setUser(user);
                        return newCategoryBudget;
                    });

            categoryBudget.setTotalAmount((categoryBudget.getTotalAmount() != null ? categoryBudget.getTotalAmount() : 0.0) + amount);
            categoryBudgetRepository.save(categoryBudget);
        });
    }
}