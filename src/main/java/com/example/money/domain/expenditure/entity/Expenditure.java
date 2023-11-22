package com.example.money.domain.expenditure.entity;

import com.example.money.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Expenditure {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long amount;
    private LocalDateTime spendTime;
    private String memo;

    @Builder.Default
    private Boolean isExcepted = false;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @Builder
    private Expenditure(Long amount, LocalDateTime spendTime, String memo, Boolean isExcepted, Member member) {
        this.amount = amount;
        this.spendTime = spendTime;
        this.memo = memo;
        this.isExcepted = isExcepted;
        this.member = member;
    }
}
