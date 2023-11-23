package com.example.money.domain.member.service;

import com.example.money.domain.member.dto.MemberLoginRequestDto;
import com.example.money.domain.member.dto.MemberSignupDto;
import com.example.money.domain.member.dto.TokenDto;
import com.example.money.domain.member.entity.Member;
import com.example.money.domain.member.repository.MemberRepository;
import com.example.money.global.jwt.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {

    private MemberRepository memberRepository;
    private PasswordEncoder passwordEncoder;
    private TokenProvider tokenProvider;

    /**
     * 회원가입 기능입니다.
     * */
    public MemberSignupDto.Response signup(MemberSignupDto.Request request) {

        // 전달 받은 request 데이터를 이용하여 member 객체를 생성하고 DB에 저장합니다.
        Member member = memberRepository.save(request.toEntity(passwordEncoder));

        // member 객체로 Response 객체를 생성하여 반환합니다.
        return MemberSignupDto.Response.from(member);
    }

    /**
     * 로그인 기능입니다.
    * */
    public TokenDto login(MemberLoginRequestDto request) {

        // 사용자의 아이디(username)으로 정보를 찾아 회원가입 했는지 확인합니다.
        Member member = memberRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("Not Found Username"));

        // 사용자가 존재할 경우 비밀번호 일치 여부를 확인 합니다.
        if(!passwordEncoder.matches(request.getPassword(), member.getPassword())) {
            throw new IllegalArgumentException("mismatched password");
        }

        // access token을 생성해 반환합니다.
        return TokenDto.of(tokenProvider.generateToken(member));
    }

}
