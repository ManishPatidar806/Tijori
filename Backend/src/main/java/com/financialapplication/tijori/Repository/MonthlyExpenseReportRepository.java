package com.financialapplication.tijori.Repository;

import com.financialapplication.tijori.Model.Entity.MonthlyExpenseReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MonthlyExpenseReportRepository extends JpaRepository<MonthlyExpenseReport, Long> {
}
