package com.potato.domain.member.repository;

import com.potato.domain.member.Member;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.potato.domain.follow.QFollow.follow;
import static com.potato.domain.member.QMember.member;

@RequiredArgsConstructor
public class MemberRepositoryCustomImpl implements MemberRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Member findMemberByEmail(String email) {
        return queryFactory.selectFrom(member)
            .where(member.email.eq(email))
            .fetchOne();
    }

    @Override
    public Member findMemberById(Long memberId) {
        return queryFactory.selectFrom(member)
            .where(
                member.id.eq(memberId)
            ).fetchOne();
    }

    @Override
    public List<Member> findFollowerMemberById(Long memberId) {
        return queryFactory.selectFrom(member)
            .innerJoin(member.followerList, follow).fetchJoin()
            .where(
               follow.memberId.eq(memberId)
            ).fetch();
    }

    @Override
    public List<Member> findToMeFollowerMemberById(Long memberId) {
        System.out.println("여기 ");
        return queryFactory.selectFrom(member)
            .innerJoin(member.followerList, follow).fetchJoin()
            .where(
                follow.follower.id.eq(memberId)
            ).fetch();
    }

}
