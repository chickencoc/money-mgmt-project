package com.example.money.domain.budgetByCategory.entity;

import com.example.money.domain.budget.entity.Budget;
import com.example.money.domain.category.entity.Category;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BudgetByCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int amount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "budget_id")
    private Budget budget;

    @Builder
    private BudgetByCategory(int amount, Category category, Budget budget) {
        this.amount = amount;
        this.category = category;
        this.budget = budget;
    }

}
