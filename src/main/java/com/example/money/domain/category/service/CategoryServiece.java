package com.example.money.domain.category.service;

import com.example.money.domain.category.dto.CategoryResponseDto;
import com.example.money.domain.category.entity.Category;
import com.example.money.domain.category.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiece {

    private final CategoryRepository categoryRepository;

    public List<CategoryResponseDto> getCategories(Long memberId) {
        return categoryRepository.findByMemberId(memberId)
                                .stream().map(CategoryResponseDto::from)
                                .toList();
    }
}
