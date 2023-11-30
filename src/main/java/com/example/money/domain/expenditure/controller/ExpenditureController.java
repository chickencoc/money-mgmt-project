package com.example.money.domain.expenditure.controller;

import com.example.money.domain.expenditure.dto.ExpenditureDto;
import com.example.money.domain.expenditure.dto.ExpenditureUpdateDto;
import com.example.money.domain.expenditure.service.ExpenditureService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/expenditures")
public class ExpenditureController {

    private final ExpenditureService expenditureService;

    @PostMapping("/new")
    public ResponseEntity<List<ExpenditureDto.Response>> saveExpenditures(@AuthenticationPrincipal User user, List<ExpenditureDto.Request> requestList) {
        return ResponseEntity.status(HttpStatus.CREATED).body(expenditureService.saveExpenditures(user, requestList));
    }

    @PatchMapping("/update")
    public ResponseEntity<ExpenditureDto.Response> updateExpenditure(@AuthenticationPrincipal User user, ExpenditureUpdateDto request) {
        return ResponseEntity.ok(expenditureService.updateExpenditure(user, request));
    }

    // todo: 지출 목록 조회

    @GetMapping("/{expenditureId}")
    public ResponseEntity<ExpenditureDto.Response> getExpenditureDetail(@PathVariable("expenditureId") Long expenditureId) {
        return ResponseEntity.ok(expenditureService.getExpenditureDetail(expenditureId));
    }

    @DeleteMapping("/{expenditureId}")
    public ResponseEntity<Void> deleteExpenditure(@PathVariable("expenditureId") Long expenditureId) {
        return ResponseEntity.noContent().build();
    }
}
