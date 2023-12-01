package com.example.money.domain.expenditure.dto;

import com.example.money.domain.expenditure.entity.Expenditure;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ExpenditureResponseDto {

    private Long id;
    private Integer amount;
    private LocalDateTime usageTime;
    private String memo;
    private Boolean isExcepted;

    public static ExpenditureResponseDto from(Expenditure expenditure) {
        return new ExpenditureResponseDto(
                expenditure.getId(),
                expenditure.getAmount(),
                expenditure.getUsageTime(),
                expenditure.getMemo(),
                expenditure.getIsExcepted()
        );
    }

}
