package com.example.money.domain.budget.entity;

import com.example.money.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Budget {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int total;
    private String name;
    private LocalDate startDate;
    private LocalDate endDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Builder
    private Budget(int total, String name, LocalDate startDate, LocalDate endDate, Member member) {
        this.total = total;
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
        this.member = member;
    }

}
