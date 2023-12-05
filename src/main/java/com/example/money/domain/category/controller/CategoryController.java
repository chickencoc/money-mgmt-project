package com.example.money.domain.category.controller;

import com.example.money.domain.category.dto.CategoryResponseDto;
import com.example.money.domain.category.service.CategoryServiece;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryServiece categoryServiece;

    @GetMapping("/list/{memberId}")
    public ResponseEntity<List<CategoryResponseDto>> getCategories(@PathVariable Long memberId) {
        return ResponseEntity.ok(categoryServiece.getCategories(memberId));
    }
}
