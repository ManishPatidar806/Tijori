package com.financialapplication.tijori.Helper.Scheduler;

import com.financialapplication.tijori.Repository.CategoryBudgetRepository;
import com.financialapplication.tijori.Service.MonthlyExpenseReportService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Scheduler for monthly data maintenance tasks.
 * Handles category budget resets and monthly expense report generation.
 *
 * @author Expense Tracker Team
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class MonthlyDataMaintenanceScheduler {

    private final CategoryBudgetRepository categoryBudgetRepository;
    private final MonthlyExpenseReportService monthlyExpenseReportService;

    /**
     * Executes monthly maintenance tasks at midnight on the first day of each month.
     * - Resets category budget totals for the new month
     * - Generates monthly expense reports for the previous month
     */
    @Scheduled(cron = "0 0 0 1 * ?") // At midnight on the 1st day of every month
    @Transactional
    public void executeMonthlyMaintenance() {
        log.info("Starting monthly maintenance tasks...");

        try {
            resetCategoryBudgets();
            generateMonthlyReports();
            log.info("Monthly maintenance tasks completed successfully");
        } catch (Exception e) {
            log.error("Error during monthly maintenance: {}", e.getMessage(), e);
            throw e;
        }
    }

    private void resetCategoryBudgets() {
        categoryBudgetRepository.updatePreviousCategoryData();
        log.info("Category budget data reset for eligible users");
    }

    private void generateMonthlyReports() {
        monthlyExpenseReportService.generateMonthlyExpenseReport();
        log.info("Monthly expense reports generated successfully");
    }
}
