package com.example.money.domain.member.dto;

import com.example.money.domain.member.entity.Member;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.crypto.password.PasswordEncoder;

public class MemberSignupDto {

    @Getter
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Request {

        @Email
        @NotBlank
        private String username;
        @NotBlank
        private String nickname;
        @NotBlank
        private String password;

        public Member toEntity(PasswordEncoder encoder) {
            return Member.builder()
                    .username(username)
                    .nickname(nickname)
                    .password(encoder.encode(password))
                    .build();
        }

        public static Request createTestEntity(String username, String nickname, String password) {
            return new Request(username, nickname, password);
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
