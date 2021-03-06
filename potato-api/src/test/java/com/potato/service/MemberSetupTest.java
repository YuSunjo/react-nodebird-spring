package com.potato.service;

import com.potato.domain.member.Member;
import com.potato.domain.member.MemberCreator;
import com.potato.domain.member.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class MemberSetupTest {

    @Autowired
    private MemberRepository memberRepository;

    protected Long memberId;

    @BeforeEach
    void setUp() {
        Member member = memberRepository.save(MemberCreator.create("tnswh2023@naver.com"));
        memberId = member.getId();
    }

    protected void cleanup() {
        memberRepository.deleteAll();
    }
}
