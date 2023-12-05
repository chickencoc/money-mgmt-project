package com.example.money.domain.budget.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class BudgetSuggestResponseDto {

    private String categoryName;
    private int amount;

    public static List<BudgetSuggestResponseDto> ListOf(Map<String, Float> categoryNameAndAverage, int total) {

        List<BudgetSuggestResponseDto> list = new ArrayList<>();

        for(String name : categoryNameAndAverage.keySet()) {
            list.add(
                    // total 값에 맞는 카테고리 별 예산 금액을 구하고, 10의 자리에서 반올림 합니다.
                    new BudgetSuggestResponseDto(name, Math.round(total * categoryNameAndAverage.get(name) / 100) * 100)
            );
        }

        return list;
    }
}
