package com.example.money.domain.category.repository;

import com.example.money.domain.category.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    List<Category> findByMemberId(Long memberId);
}
