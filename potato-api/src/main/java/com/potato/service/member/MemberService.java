package com.potato.service.member;

import com.potato.domain.member.Member;
import com.potato.domain.member.MemberRepository;
import com.potato.service.member.request.CreateMemberRequest;
import com.potato.service.member.request.UpdateMemberRequest;
import com.potato.service.member.response.MemberInfoResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
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
        System.out.println("여기는");
        List<Member> followerList = memberRepository.findFollowerMemberById(member.getId());
        return followerList.stream()
            .map(MemberInfoResponse::of)
            .collect(Collectors.toList());
    }

}
