package com.example.money.domain.member.dto;

import com.example.money.domain.member.entity.Member;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.crypto.password.PasswordEncoder;

public class MemberSignupDto {

    @Getter
    public static class Request {

        private String username;
        private String nickname;
        private String password;

        public Member toEntity(PasswordEncoder encoder) {
            return Member.builder()
                    .username(username)
                    .nickname(nickname)
                    .password(encoder.encode(password))
                    .build();
        }
    }

    @Getter
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Response {

        private String username;
        private String nickname;

        public static Response from(Member member) {
            return new Response(member.getUsername(), member.getNickname());
        }
    }

}
