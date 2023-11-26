package com.example.money.domain.category.controller;

import com.example.money.IntegrationTest;
import com.example.money.domain.category.entity.Category;
import com.example.money.domain.member.entity.Member;
import com.example.money.global.setup.CategorySetup;
import com.example.money.global.setup.MemberSetup;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.ResultActions;


import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.junit.jupiter.api.Assertions.*;

class CategoryControllerTest extends IntegrationTest {

    @Autowired
    private MemberSetup memberSetup;
    @Autowired
    private CategorySetup categorySetup;

    @Test
    void getCategories() throws Exception {
        // given
        Member member = memberSetup.createOne();
        List<Category> categoryList = categorySetup.saveDefaultCategories(member);

        // when
        ResultActions resultActions =  mvc.perform(
                get("/api/categories/" + member.getId())
        );

        // then
        resultActions
                .andDo(print())
                .andExpect(status().isOk())
                // 기본으로 저장되는 카테고리가 4개인지 확인
                .andExpectAll(jsonPath("$[3]").exists(), jsonPath("$[4]").doesNotExist())
                .andExpect(jsonPath("$[3].name").value("기타"));
    }

}