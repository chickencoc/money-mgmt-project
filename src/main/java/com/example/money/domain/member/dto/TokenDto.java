package com.example.money.domain.member.dto;

public class TokenDto {

    private String accessToken;

    private TokenDto(String accessToken) {
        this.accessToken = accessToken;
    }

    public static TokenDto of(String accessToken) {
        return new TokenDto(accessToken);
    }
}
