package com.example.money.domain.category.service;

import com.example.money.domain.category.entity.Category;
import com.example.money.domain.category.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiece {

    private final CategoryRepository categoryRepository;

    public List<Category> getCategories(Long memberId) {
        return categoryRepository.findByMemberId(memberId);
    }
}
