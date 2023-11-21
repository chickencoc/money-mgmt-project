package com.example.money.domain.member.dto;

import com.example.money.domain.member.entity.Member;
import lombok.Getter;

@Getter
public class MemberSignupRequest {

    private String username;
    private String nickname;
    private String password;

    public Member toEntity() {
        return Member.builder()
                .username(username)
                .nickname(nickname)
                .password(password)
                .build();
    }
}
