package com.potato.service.member;

import com.potato.domain.member.Member;
import com.potato.domain.member.MemberRepository;
import com.potato.exception.ConflictException;
import com.potato.exception.NotFoundException;

public class MemberServiceUtils {
    public static void validateNonExistsMember(MemberRepository memberRepository, String email) {
        Member member = memberRepository.findMemberByEmail(email);
        if (member != null) {
            throw new ConflictException(String.format("이미 존재하는 회원 (%s) 입니다.", email));
        }
    }

    public static Member findMemberById(MemberRepository memberRepository, Long memberId) {
        Member member = memberRepository.findMemberById(memberId);
        if (member == null) {
            throw new NotFoundException(String.format("해당 회원 (%s)는 존재하지 않습니다.", memberId));
        }
        return member;
    }
}
