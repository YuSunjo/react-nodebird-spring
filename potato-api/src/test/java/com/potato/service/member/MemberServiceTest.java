package com.potato.service.member;

import com.potato.domain.member.Member;
import com.potato.domain.member.MemberCreator;
import com.potato.domain.member.MemberRepository;
import com.potato.service.member.request.CreateMemberRequest;
import com.potato.service.member.response.MemberInfoResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
public class MemberServiceTest {

    @Autowired
    private MemberService memberService;

    @Autowired
    private MemberRepository memberRepository;

    @AfterEach
    void cleanUp() {
        memberRepository.deleteAll();
    }

    @Test
    void 회원가입을_진행한다() {
        //given
        String email = "tnswh2023@naver.com";
        String name = "유순조";
        String profileUrl = "http://profile.com";

        CreateMemberRequest request = CreateMemberRequest.testBuilder()
            .email(email)
            .name(name)
            .profileUrl(profileUrl)
            .build();

        //when
        memberService.createMember(request);

        //then
        // then
        List<Member> memberList = memberRepository.findAll();
        assertThat(memberList).hasSize(1);
        assertMemberInfo(memberList.get(0), email, name, profileUrl);
    }

    @Test
    void 이미_회원가입한_회원일_경우_에러가_발생한다() {
        // given
        String email = "will.seungho@gmail.com";
        memberRepository.save(MemberCreator.create(email));

        CreateMemberRequest request = CreateMemberRequest.testBuilder()
            .email(email)
            .name("강승호")
            .build();

        // when & then
        assertThatThrownBy(() -> {
            memberService.createMember(request);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    private void assertMemberInfo(Member member, String email, String name, String profileUrl) {
        assertThat(member.getEmail()).isEqualTo(email);
        assertThat(member.getName()).isEqualTo(name);
        assertThat(member.getProfileUrl()).isEqualTo(profileUrl);
    }

    @Test
    void 회원_정보를_불러온다() {
        // given
        String email = "tnswh2023@naver.com";
        String name = "유순조";
        String profileUrl = "http://profile.com";

        Member member = memberRepository.save(MemberCreator.create(email, name, profileUrl));

        // when
        MemberInfoResponse response = memberService.getMemberInfo(member.getId());

        // then
        assertThatMemberInfoResponse(response, email, name, profileUrl);
    }

    @Test
    void 존재하지_않는_멤버의_회원정보를_불러오면_에러가_발생한다() {
        // when & then
        assertThatThrownBy(() -> {
            memberService.getMemberInfo(999L);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    private void assertThatMemberInfoResponse(MemberInfoResponse response, String email, String name, String profileUrl) {
        assertThat(response.getEmail()).isEqualTo(email);
        assertThat(response.getName()).isEqualTo(name);
        assertThat(response.getProfileUrl()).isEqualTo(profileUrl);
    }


}