package com.potato.service.member;

import com.potato.domain.member.Member;
import com.potato.domain.member.MemberRepository;
import com.potato.service.member.request.CreateMemberRequest;
import com.potato.service.member.request.UpdateMemberRequest;
import com.potato.service.member.response.MemberInfoResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class MemberService {

    private final MemberRepository memberRepository;

    @Transactional
    public Long createMember(CreateMemberRequest request) {
        MemberServiceUtils.validateNonExistsMember(memberRepository, request.getEmail());
        return memberRepository.save(request.toEntity()).getId();
    }

    @Transactional(readOnly = true)
    public MemberInfoResponse getMemberInfo(Long memberId) {
        Member member = MemberServiceUtils.findMemberById(memberRepository, memberId);

        return MemberInfoResponse.of(member);
    }

    @Transactional
    public MemberInfoResponse updateMemberInfo(UpdateMemberRequest request, Long memberId) {
        Member member = MemberServiceUtils.findMemberById(memberRepository, memberId);
        member.update(request.getNickname());

        return MemberInfoResponse.of(member);
    }

    @Transactional
    public void followMember(Long targetId, Long memberId) {
        Member followerMember = MemberServiceUtils.findMemberById(memberRepository, targetId);
        followerMember.addFollowing(memberId);
    }

    @Transactional(readOnly = true)
    public List<MemberInfoResponse> getFollowerMember(Long memberId) {
        Member member = MemberServiceUtils.findMemberById(memberRepository, memberId);
        List<Member> followerList = memberRepository.findFollowerMemberById(member.getId());
//        List<Member> followerList = memberRepository.findAllById(member.getFollowerId());
        return followerList.stream()
            .map(MemberInfoResponse::of)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<MemberInfoResponse> getToMeFollowerMember(Long memberId) {
        Member member = MemberServiceUtils.findMemberById(memberRepository, memberId);
//        List<Member> toMeFollowerList = memberRepository.findToMeFollowerMemberById(member.getId());
        List<Member> toMeFollowerList = memberRepository.findAllById(member.getFollowerId());
        return toMeFollowerList.stream()
            .map(MemberInfoResponse::of)
            .collect(Collectors.toList());
    }

    @Transactional
    public void unFollowMember(Long targetId, Long memberId) {
        Member targetMember = MemberServiceUtils.findMemberById(memberRepository, targetId);
        targetMember.unFollowing(targetMember, memberId);
    }

}
