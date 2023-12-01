package com.example.money.domain.expenditure.dto;

import com.example.money.domain.expenditure.entity.Expenditure;
import com.example.money.domain.member.entity.Member;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

public class ExpenditureDto {

    @Getter
    public static class Request {

        private Integer amount;
        private LocalDateTime usageTime;
        private String memo;
        private Boolean isExcepted;
        private Long categoryId;
        private Long budgetId;

        public Expenditure toExpenditure(Member member) {
            return Expenditure.builder()
                    .amount(amount)
                    .usageTime(usageTime)
                    .memo(memo)
                    .isExcepted(isExcepted)
                    .member(member)
                    .build();
        }
    }

    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Response {

        private Long id;
        private Integer amount;
        private LocalDateTime spendTime;
        private String memo;
        private Boolean isExcepted;

        public static ExpenditureDto.Response from(Expenditure expenditure) {
            return new ExpenditureDto.Response(
                    expenditure.getId(),
                    expenditure.getAmount(),
                    expenditure.getUsageTime(),
                    expenditure.getMemo(),
                    expenditure.getIsExcepted()
            );
    }

    }
}
