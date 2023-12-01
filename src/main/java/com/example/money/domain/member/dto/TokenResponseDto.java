package com.example.money.domain.member.dto;

import lombok.Getter;

@Getter
public class TokenResponseDto {

    private String accessToken;

    private TokenResponseDto(String accessToken) {
        this.accessToken = accessToken;
    }

    public static TokenResponseDto of(String accessToken) {
        return new TokenResponseDto(accessToken);
    }
}
