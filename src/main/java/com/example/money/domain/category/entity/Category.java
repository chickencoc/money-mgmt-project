package com.example.money.domain.category.entity;

import com.example.money.domain.budgetByCategory.entity.BudgetByCategory;
import com.example.money.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    private Category(String name, Member member) {
        this.name = name;
        this.member = member;
    }

    /**
     * 주어진 명칭에 해당하는 새로운 카테고리를 생성합니다.
     * */
    public static Category customOf(String name, Member member) {
        return new Category(name, member);
    }

    /**
     * 기본 카테고리 생성합니다. 식비, 생활비, 여가, 기타가 기본으로 생성됩니다.
    * */
    public static List<Category> createDefaultCategories(Member member) {

        String[] names = {"식비", "생활비", "여가", "기타"};
        ArrayList<String> categoryNames = new ArrayList<>(Arrays.asList(names));

        return categoryNames.stream()
                            .map(name -> new Category(name, member))
                            .toList();
    }

}
