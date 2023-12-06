package com.example.money.domain.expenditure.controller;

import com.example.money.domain.expenditure.dto.ExpenditureCreateRequestDto;
import com.example.money.domain.expenditure.dto.ExpenditureResponseDto;
import com.example.money.domain.expenditure.dto.ExpenditureSearchDto;
import com.example.money.domain.expenditure.dto.ExpenditureUpdateRequestDto;
import com.example.money.domain.expenditure.service.ExpenditureService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/expenditures")
public class ExpenditureController {

    private final ExpenditureService expenditureService;

    @PostMapping("/new")
    public ResponseEntity<ExpenditureResponseDto> saveExpenditures(@AuthenticationPrincipal User user, ExpenditureCreateRequestDto request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(expenditureService.saveExpenditures(user, request));
    }

    @PatchMapping("/update")
    public ResponseEntity<ExpenditureResponseDto> updateExpenditure(@AuthenticationPrincipal User user, ExpenditureUpdateRequestDto request) {
        return ResponseEntity.ok(expenditureService.updateExpenditure(user, request));
    }

    /**
    * @param startDate - 시작일
    * @param endDate - 종료일
    * @param categoryName - 카테고리 명칭
    * @param minAmount - 최소 금액 조건
    * @param maxAmount - 최대 금액 조건
    * */
    @GetMapping("/list")
    public ResponseEntity<ExpenditureSearchDto.Response> getExpendituresByCondition(@AuthenticationPrincipal User user,
                                                                                    @RequestParam("startDate") LocalDate startDate,
                                                                                    @RequestParam("endDate") LocalDate endDate,
                                                                                    @RequestParam("categoryName") String categoryName,
                                                                                    @RequestParam("minAmount") Integer minAmount,
                                                                                    @RequestParam("maxAmount") Integer maxAmount) {
        return ResponseEntity.ok(expenditureService.getExpendituresByCondition(user, startDate, endDate, categoryName, minAmount, maxAmount));
    }

    @GetMapping("/{expenditureId}")
    public ResponseEntity<ExpenditureResponseDto> getExpenditureDetail(@PathVariable("expenditureId") Long expenditureId) {
        return ResponseEntity.ok(expenditureService.getExpenditureDetail(expenditureId));
    }

    @DeleteMapping("/{expenditureId}")
    public ResponseEntity<Void> deleteExpenditure(@PathVariable("expenditureId") Long expenditureId) {
        expenditureService.deleteExpenditure(expenditureId);
        return ResponseEntity.noContent().build();
    }
}
