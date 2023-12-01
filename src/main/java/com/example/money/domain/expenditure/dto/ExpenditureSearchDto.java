package com.example.money.domain.expenditure.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.Map;

public class ExpenditureSearchDto {

    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Response {
        private List<ExpenditureDto.Response> ExpenditureList;
        private Map<String, Integer> totalByCategory;
        private Integer totalSum;

        public static Response from(List<ExpenditureDto.Response> expenditureList, Map<String, Integer> totalByCategory, Integer totalSum) {
            return new Response(expenditureList, totalByCategory, totalSum);
        }
    }
}
