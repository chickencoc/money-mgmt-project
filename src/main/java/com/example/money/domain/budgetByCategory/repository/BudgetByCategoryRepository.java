package com.example.money.domain.budgetByCategory.repository;

import com.example.money.domain.budgetByCategory.entity.BudgetByCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface BudgetByCategoryRepository extends JpaRepository<BudgetByCategory, Long> {

    @Query(
            "select bbc" +
            "from BudgetByCategory bbc" +
            "join fetch bbc.category" +
            "join fetch bbc.budget" +
            "where bbc.budget.startDate >= :startDate"
    )
    List<BudgetByCategory> findAllBudgetByCategoryInOneMonthWithCategoryAndBudget(@Param("startDate") LocalDate startDate);
}
