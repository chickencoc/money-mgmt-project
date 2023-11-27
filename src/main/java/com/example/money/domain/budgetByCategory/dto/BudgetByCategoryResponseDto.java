package com.example.money.domain.budgetByCategory.dto;

import com.example.money.domain.budgetByCategory.entity.BudgetByCategory;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class BudgetByCategoryResponseDto {

    private Long id;
    private int amount;
    private String categoryName;

    public static BudgetByCategoryResponseDto from(BudgetByCategory budgetByCategory) {
        return new BudgetByCategoryResponseDto(
                budgetByCategory.getId(),
                budgetByCategory.getAmount(),
                budgetByCategory.getCategory().getName()
        );
    }
}
