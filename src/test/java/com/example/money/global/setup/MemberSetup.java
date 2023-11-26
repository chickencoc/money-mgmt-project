package com.example.money.global.setup;

import com.example.money.domain.member.entity.Member;
import com.example.money.domain.member.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class MemberSetup {

    @Autowired
    private MemberRepository memberRepository;

    /**
     * 테스트용 Member 데이터를 생성합니다.
     * @param count 생성할 데이터의 수를 입니다.
     * */
    private List<Member> buildDummy(int count) {
        ArrayList<Member> members =  new ArrayList<>();

        for(int i = 0; i < count; i++) {
            members.add(
                    Member.builder()
                            .username("user" + i + "@gmail.com")
                            .nickname("testuser" + i)
                            .password("test1234")
                            .build()
            );
        }

        return members;
    }

    /**
     * 테스트를 위한 하나의 Member 데이터를 생성합니다.
     * */
    public Member createOne() {
        return memberRepository.saveAll(buildDummy(1)).get(0);
    }

    /**
     * 여러 개의 Member 데이터를 생성합니다.
     * */
    public List<Member> createMultiple(int count) {
        return memberRepository.saveAll(buildDummy(count));
    }
}
