package com.example.money.global.setup;

import com.example.money.domain.category.entity.Category;
import com.example.money.domain.category.repository.CategoryRepository;
import com.example.money.domain.member.entity.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CategorySetup {

    @Autowired
    private CategoryRepository categoryRepository;

    /**
     * 테스트용 Member의 기본 카테고리를 생성합니다.
     * */
    public List<Category> saveDefaultCategories(Member member) {
        return categoryRepository.saveAll(Category.createDefaultCategories(member));
    }

    /**
     * 테스트용 Member 데이터를 추가적인 카테고리를 생성합니다.
     * */
    public Category saveCustomCategory(String name, Member member) {
        return categoryRepository.save(Category.customOf(name, member));
    }


}
