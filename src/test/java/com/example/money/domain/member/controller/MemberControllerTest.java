package com.example.money.domain.member.controller;

import com.example.money.IntegrationTest;
import com.example.money.domain.member.dto.MemberSignupDto;
import com.example.money.global.setup.MemberSetup;
import org.hibernate.sql.ast.tree.cte.CteSearchClauseKind;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

class MemberControllerTest extends IntegrationTest {

    @Autowired
    private MemberSetup memberSetup;

    private ResultActions resultActionOfSignup(MemberSignupDto.Request request) throws Exception {
        return mvc.perform(
                post("/api/members/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        );
    }

    @Test
    void signup_정상적인_회원가입() throws Exception {
        // given
        String username = "testuser@test.com";
        String nickname = "testuser";
        String password = "test1234";

        MemberSignupDto.Request signupRequest = MemberSignupDto.Request.createTestEntity(username, nickname, password);

        // when
        resultActionOfSignup(signupRequest)
                // then
                .andExpect(status().isCreated())
                .andExpect(jsonPath("username").value(username))
                .andExpect(jsonPath("nickname").value(nickname));
    }

    @Test
    void signup_이메일_형식_오류() throws Exception {
        // given
        String username = "testuser%test.com";
        String nickname = "testuser";
        String password = "test1234";

        MemberSignupDto.Request signupRequest = MemberSignupDto.Request.createTestEntity(username, nickname, password);

        // when
        resultActionOfSignup(signupRequest)
                // then
                .andExpect(status().is4xxClientError());
    }

    @Test
    void signup_닉네임_미입력_오류() throws Exception {

    }

}