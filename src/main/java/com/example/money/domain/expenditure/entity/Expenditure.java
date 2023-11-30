package com.example.money.domain.expenditure.entity;

import com.example.money.domain.budgetByCategory.entity.BudgetByCategory;
import com.example.money.domain.expenditure.dto.ExpenditureUpdateDto;
import com.example.money.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Optional;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Expenditure {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int amount;
    private LocalDateTime spendTime;
    private String memo;

    private Boolean isExcepted;

    @ManyToOne(fetch = FetchType.LAZY)
    private BudgetByCategory budgetByCategory;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @Builder
    private Expenditure(int amount, LocalDateTime spendTime, String memo, Boolean isExcepted, BudgetByCategory budgetByCategory, Member member) {
        this.amount = amount;
        this.spendTime = spendTime;
        this.memo = memo;
        this.isExcepted = Optional.ofNullable(isExcepted).orElse(true);
        this.budgetByCategory = budgetByCategory;
        this.member = member;
    }

    public void update(ExpenditureUpdateDto request) {
        this.amount = Optional.ofNullable(request.getAmount()).orElse(this.amount);
        this.spendTime = Optional.ofNullable(request.getSpendTime()).orElse(this.spendTime);
        this.memo = Optional.ofNullable(request.getMemo()).orElse(this.memo);
        this.isExcepted = Optional.ofNullable(request.getIsExcepted()).orElse(this.isExcepted);
    }
}
