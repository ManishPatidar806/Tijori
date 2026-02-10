package com.financialapplication.tijori.Service;

import com.financialapplication.tijori.Model.Entity.MonthlyExpenseReport;
import com.financialapplication.tijori.Model.Entity.AccountBalance;
import com.financialapplication.tijori.Repository.MonthlyExpenseReportRepository;
import com.financialapplication.tijori.Repository.AccountBalanceRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class MonthlyExpenseReportService {

    private final AccountBalanceRepository accountBalanceRepository;
    private final MonthlyExpenseReportRepository monthlyExpenseReportRepository;

    public MonthlyExpenseReportService(AccountBalanceRepository accountBalanceRepository,
                                        MonthlyExpenseReportRepository monthlyExpenseReportRepository) {
        this.accountBalanceRepository = accountBalanceRepository;
        this.monthlyExpenseReportRepository = monthlyExpenseReportRepository;
    }

    @Transactional
    public void generateMonthlyExpenseReport() {
        List<AccountBalance> summaries = accountBalanceRepository.findExactlyOneMonthOldEntries();
        if (summaries.isEmpty()) {
            return;
        }
        LocalDateTime now = LocalDateTime.now();
        LocalDate lastMonth = now.toLocalDate().minusMonths(1);
        List<MonthlyExpenseReport> entities = new ArrayList<>();

        for (AccountBalance accountBalance : summaries) {
            MonthlyExpenseReport report = createMonthlyExpenseReport(accountBalance, now, lastMonth);
            entities.add(report);
            accountBalance.setMonthlyLimit(accountBalance.getBudgetLimit());
            accountBalance.setTotalCredits(0.0);
            accountBalance.setTotalDebits(0.0);
            accountBalance.setTotalSavings(0.0);
            accountBalance.setStartDate(now);
        }

        monthlyExpenseReportRepository.saveAll(entities);
        accountBalanceRepository.saveAll(summaries);
    }

    private MonthlyExpenseReport createMonthlyExpenseReport(AccountBalance accountBalance, LocalDateTime now, LocalDate lastMonth) {
        double income = accountBalance.getIncome() != null ? accountBalance.getIncome() : 0.0;
        double debited = accountBalance.getTotalDebits() != null ? accountBalance.getTotalDebits() : 0.0;
        double credited = accountBalance.getTotalCredits() != null ? accountBalance.getTotalCredits() : 0.0;
        double netSavings = income - debited;

        MonthlyExpenseReport report = new MonthlyExpenseReport();
        report.setNetSavings(Math.max(netSavings, 0));
        report.setIncome(income);
        report.setUser(accountBalance.getUser());
        report.setTotalCredits(credited);
        report.setTotalDebits(debited);
        report.setRecordedAt(now);
        report.setMonth(lastMonth.getMonth());
        report.setYear(lastMonth.getYear());
        return report;
    }
}
