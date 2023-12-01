package com.example.money.domain.budget.controller;

import com.example.money.domain.budget.dto.BudgetSetDto;
import com.example.money.domain.budget.service.BudgetService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/budgets")
public class BudgetController {

    private final BudgetService budgetService;

    @PostMapping("/set")
    public ResponseEntity<BudgetSetDto.Response> setBudget(@AuthenticationPrincipal User user, @RequestBody BudgetSetDto.Request request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(budgetService.setBudget(user, request));
    }

    // todo: 기능 구현 필요
    @GetMapping("/suggest")
    public ResponseEntity<Void> setSuggestedBudget(@RequestParam("total") int total) {
        return null;
    }
}
