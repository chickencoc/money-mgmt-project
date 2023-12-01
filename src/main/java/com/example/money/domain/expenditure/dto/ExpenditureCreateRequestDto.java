package com.example.money.domain.expenditure.dto;

import com.example.money.domain.budgetByCategory.entity.BudgetByCategory;
import com.example.money.domain.expenditure.entity.Expenditure;
import com.example.money.domain.member.entity.Member;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ExpenditureCreateRequestDto {

    private Integer amount;
    private LocalDateTime usageTime;
    private String memo;
    private Boolean isExcepted;
    private Long categoryId;
    private Long budgetId;

    public Expenditure toExpenditure(BudgetByCategory budgetByCategory, Member member) {
        return Expenditure.builder()
                .amount(amount)
                .usageTime(usageTime)
                .memo(memo)
                .isExcepted(isExcepted)
                .budgetByCategory(budgetByCategory)
                .member(member)
                .build();
    }
}
