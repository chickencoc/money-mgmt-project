package com.example.money.domain.expenditure.dto;

import com.example.money.domain.expenditure.entity.Expenditure;
import com.example.money.domain.member.entity.Member;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ExpenditureUpdateDto {

    private Long id;
    private Integer amount;
    private LocalDateTime usageTime;
    private String memo;
    private Boolean isExcepted;
    private Long categoryId;
    private Long budgetId;

    public Expenditure toEntity(Member member) {
        return Expenditure.builder()
                .amount(amount)
                .usageTime(usageTime)
                .memo(memo)
                .isExcepted(isExcepted)
                .member(member)
                .build();
    }
}
