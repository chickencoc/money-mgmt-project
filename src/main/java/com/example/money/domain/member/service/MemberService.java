package com.example.money.domain.member.service;

import com.example.money.domain.category.entity.Category;
import com.example.money.domain.category.repository.CategoryRepository;
import com.example.money.domain.member.dto.MemberLoginRequestDto;
import com.example.money.domain.member.dto.MemberSignupDto;
import com.example.money.domain.member.dto.TokenResponseDto;
import com.example.money.domain.member.entity.Member;
import com.example.money.domain.member.repository.MemberRepository;
import com.example.money.global.jwt.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final CategoryRepository categoryRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;

    /**
     * 회원가입 기능입니다.
     * */
    public MemberSignupDto.Response signup(MemberSignupDto.Request request) {

        // 아이디 중복 확인
        if(memberRepository.existsByUsername(request.getUsername()))
            throw new IllegalArgumentException("Already Exist");

        // 전달 받은 request 데이터를 이용하여 member 객체를 생성 후 DB에 저장
        Member member = memberRepository.save(request.toEntity(passwordEncoder));

        // 사용자의 기본 카테고리 생성 및 DB에 저장
        categoryRepository.saveAll(Category.createDefaultCategories(member));

        // member 객체로 Response 객체를 생성 및 반환
        return MemberSignupDto.Response.from(member);
    }

    /**
     * 로그인 기능입니다.
    * */
    public TokenResponseDto login(MemberLoginRequestDto request) {

        // username(아이디)로 사용자의 회원가입 여부 확인
        Member member = memberRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("Not Found Username"));

        // 비밀번호 일치 여부를 확인
        if(!member.matchesPassword(request, passwordEncoder)) {
            throw new IllegalArgumentException("mismatched password");
        }

        // access token 생성 및 반환
        return TokenResponseDto.of(tokenProvider.generateToken(member));
    }

}
