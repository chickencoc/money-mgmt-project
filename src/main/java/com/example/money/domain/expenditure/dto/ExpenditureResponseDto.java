package com.example.money.domain.expenditure.dto;

import com.example.money.domain.expenditure.entity.Expenditure;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ExpenditureResponseDto {

    // todo: category와 budget을 확인할 수 없음
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
