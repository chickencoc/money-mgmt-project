package com.example.money.domain.budget.repository;

import com.example.money.domain.budget.entity.Budget;
import com.example.money.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface BudgetRepository extends JpaRepository<Budget, Long> {

    boolean existByStartDateAndEndDateAndMemberId(LocalDate startDate, LocalDate endDate, Long memberId);
}
