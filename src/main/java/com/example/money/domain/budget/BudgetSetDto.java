package com.example.money.domain.budget;

import com.example.money.domain.budget.entity.Budget;
import com.example.money.domain.budgetByCategory.dto.BudgetByCategoryResponseDto;
import com.example.money.domain.budgetByCategory.entity.BudgetByCategory;
import com.example.money.domain.category.entity.Category;
import com.example.money.domain.member.entity.Member;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class BudgetSetDto {

    @Getter
    public static class Request {

        private String name;
        private int total;
        private LocalDate startDate;
        private LocalDate endDate;

        private Map<Long, Integer> categoryIdAndAmount;

        public Budget toBudget(Member member) {
            return Budget.builder()
                    .name(name)
                    .total(total)
                    .startDate(startDate)
                    .endDate(endDate)
                    .member(member)
                    .build();
        }

    }

    @Getter
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Response {

        private Long id;
        private String name;
        private int total;
        private LocalDate startDate;
        private LocalDate endDate;
        private List<BudgetByCategoryResponseDto> budgetByCategories;

        public static Response from(Budget budget, List<BudgetByCategory> budgetByCategories) {
            List<BudgetByCategoryResponseDto> list = budgetByCategories.stream().map(BudgetByCategoryResponseDto::from).toList();
            return new Response(budget.getId(), budget.getName(), budget.getTotal(), budget.getStartDate(), budget.getEndDate(), list);
        }
    }

}
