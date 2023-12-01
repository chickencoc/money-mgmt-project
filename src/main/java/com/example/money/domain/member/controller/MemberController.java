package com.example.money.domain.member.controller;

import com.example.money.domain.member.dto.MemberLoginRequestDto;
import com.example.money.domain.member.dto.MemberSignupDto;
import com.example.money.domain.member.dto.TokenDto;
import com.example.money.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/signup")
    public ResponseEntity<MemberSignupDto.Response> signup(@RequestBody MemberSignupDto.Request request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(memberService.signup(request));
    }

    @PostMapping("/login")
    public ResponseEntity<TokenDto> login(@RequestBody MemberLoginRequestDto request) {
        return ResponseEntity.ok(memberService.login(request));
    }
}
