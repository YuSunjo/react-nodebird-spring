package com.potato.domain.member.repository;

import com.potato.domain.member.Member;

import java.util.List;

public interface MemberRepositoryCustom {

    Member findMemberByEmail(String email);

    Member findMemberById(Long memberId);

    List<Member> findFollowerMemberById(Long id);
}
