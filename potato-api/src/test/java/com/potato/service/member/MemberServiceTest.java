package com.potato.service.member;

import com.potato.domain.follow.Follow;
import com.potato.domain.follow.FollowRepository;
import com.potato.domain.member.Member;
import com.potato.domain.member.MemberCreator;
import com.potato.domain.member.MemberRepository;
import com.potato.exception.ConflictException;
import com.potato.exception.NotFoundException;
import com.potato.service.member.request.CreateMemberRequest;
import com.potato.service.member.request.UpdateMemberRequest;
import com.potato.service.member.response.MemberInfoResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
public class MemberServiceTest {

    @Autowired
    private MemberService memberService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private FollowRepository followRepository;

    @AfterEach
    void cleanUp() {
        memberRepository.deleteAll();
        followRepository.deleteAll();
    }

    @Test
    void 회원가입을_진행한다() {
        //given
        String email = "tnswh2023@naver.com";
        String nickname = "유순조";
        String profileUrl = "http://profile.com";

        CreateMemberRequest request = CreateMemberRequest.testBuilder()
            .email(email)
            .nickname(nickname)
            .profileUrl(profileUrl)
            .build();

        //when
        memberService.createMember(request);

        //then
        // then
        List<Member> memberList = memberRepository.findAll();
        assertThat(memberList).hasSize(1);
        assertMemberInfo(memberList.get(0), email, nickname, profileUrl);
    }

    @Test
    void 이미_회원가입한_회원일_경우_에러가_발생한다() {
        // given
        String email = "tnswh2023@naver.com";
        memberRepository.save(MemberCreator.create(email));

        CreateMemberRequest request = CreateMemberRequest.testBuilder()
            .email(email)
            .nickname("유순조")
            .build();

        // when & then
        assertThatThrownBy(() -> {
            memberService.createMember(request);
        }).isInstanceOf(ConflictException.class);
    }

    private void assertMemberInfo(Member member, String email, String nickname, String profileUrl) {
        assertThat(member.getEmail()).isEqualTo(email);
        assertThat(member.getNickname()).isEqualTo(nickname);
        assertThat(member.getProfileUrl()).isEqualTo(profileUrl);
    }

    @Test
    void 회원_정보를_불러온다() {
        // given
        String email = "tnswh2023@naver.com";
        String nickname = "유순조";
        String profileUrl = "http://profile.com";

        Member member = memberRepository.save(MemberCreator.create(email, nickname, profileUrl));

        // when
        MemberInfoResponse response = memberService.getMemberInfo(member.getId());

        // then
        assertThatMemberInfoResponse(response, email, nickname, profileUrl);
    }

    @Test
    void 존재하지_않는_멤버의_회원정보를_불러오면_에러가_발생한다() {
        // when & then
        assertThatThrownBy(() -> {
            memberService.getMemberInfo(999L);
        }).isInstanceOf(NotFoundException.class);
    }

    private void assertThatMemberInfoResponse(MemberInfoResponse response, String email, String nickname, String profileUrl) {
        assertThat(response.getEmail()).isEqualTo(email);
        assertThat(response.getNickname()).isEqualTo(nickname);
        assertThat(response.getProfileUrl()).isEqualTo(profileUrl);
    }

    @Test
    void 닉네임_변경하기() {
        //given
        Member member = MemberCreator.create("tnswh2023@naver.com");
        memberRepository.save(member);

        String nickname= "updateNickname";

        UpdateMemberRequest request = UpdateMemberRequest.testInstance(nickname);

        //when
        memberService.updateMemberInfo(request, member.getId());

        //then
        assertMemberInfo(member, member.getEmail(), member.getNickname(), member.getProfileUrl());
    }

    @Test
    void 존재하지_않는_멤버의_닉네임을_변경하려고한다() {
        //given
        String nickname= "updateNickname";

        UpdateMemberRequest request = UpdateMemberRequest.testInstance(nickname);

        //when & then
        assertThatThrownBy(
            () -> memberService.updateMemberInfo(request, 11L)
        ).isInstanceOf(NotFoundException.class);
    }

    @Test
    public void 내가_누군가를_팔로잉하면_정상처리된다() {
        //given
        Member following = MemberCreator.create("tnswh2023@naver.com");
        memberRepository.save(following);

        Member follower = MemberCreator.create("tnswh2020@naver.com");
        memberRepository.save(follower);

        //when
        memberService.followMember(follower.getId(), following.getId());

        //then
        List<Member> memberList = memberRepository.findAll();
        assertThat(memberList.get(1).getFollowerCount()).isEqualTo(1);

        List<Follow> followList = followRepository.findAll();
        assertThat(followList).hasSize(1);
        assertThat(followList.get(0).getFollower().getId()).isEqualTo(follower.getId());
    }

    @Test
    void 팔로우하려는_멤버가_없을_경우() {
        //given
        Member following = MemberCreator.create("tnswh2023@naver.com");
        memberRepository.save(following);

        Long follower = 99L;

        //when & then
        assertThatThrownBy(
            () -> memberService.followMember(follower, following.getId())
        ).isInstanceOf(NotFoundException.class);
    }

    @Test
    public void 내가_팔로워한_멤버들을_가져온다() {
        //given
        Member member1 = MemberCreator.create("tnswh1@naver.com");
        memberRepository.save(member1);

        Member member2 = MemberCreator.create("tnswh2@naver.com");
        Member member3 = MemberCreator.create("tnswh3@naver.com");

        member2.addFollowing(member1.getId());
        member3.addFollowing(member1.getId());
        memberRepository.saveAll(Arrays.asList(member2, member3));

        //when
        List<MemberInfoResponse> responses = memberService.getFollowerMember(member1.getId());
        System.out.println("responses = " + responses);

        //then
        assertThat(responses.get(0).getEmail()).isEqualTo("tnswh2@naver.com");
        assertThat(responses.get(1).getEmail()).isEqualTo("tnswh3@naver.com");
    }

    @Test
    void 나를_팔로우한_멤버들을_불러온다() {
        //given
        Member member1 = MemberCreator.create("tnswh1@naver.com");


        Member member2 = MemberCreator.create("tnswh2@naver.com");
        Member member3 = MemberCreator.create("tnswh3@naver.com");
        memberRepository.saveAll(Arrays.asList(member2, member3));

        member1.addFollowing(member2.getId());
        member1.addFollowing(member3.getId());

        memberRepository.save(member1);


        //when
        List<MemberInfoResponse> responses = memberService.getToMeFollowerMember(member1.getId());
        for (MemberInfoResponse response : responses) {
            System.out.println("response = " + response.getEmail());
        }

        //then
        assertThat(responses.get(0).getEmail()).isEqualTo(member2.getEmail());
        assertThat(responses.get(1).getEmail()).isEqualTo(member3.getEmail());

    }

}
