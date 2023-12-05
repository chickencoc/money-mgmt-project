package com.example.money.domain.category.dto;

import com.example.money.domain.category.entity.Category;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CategoryResponseDto {

    private Long id;
    private String name;

    public static CategoryResponseDto from(Category category) {
        return new CategoryResponseDto(category.getId(), category.getName());
    }
}
