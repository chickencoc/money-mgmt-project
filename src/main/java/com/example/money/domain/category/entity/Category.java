package com.example.money.domain.category.entity;

import com.example.money.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @Builder
    public Category(String name, Member member) {
        this.name = name;
        this.member = member;
    }

    public static List<Category> createDefaultCategories(Member member) {

        String[] names = {"식비", "생활비", "여가", "기타"};
        ArrayList<String> categoryNames = new ArrayList<>(Arrays.asList(names));

        return categoryNames.stream()
                            .map(name -> Category.builder()
                                                .name(name)
                                                .member(member)
                                                .build()
                            )
                            .toList();
    }

}
