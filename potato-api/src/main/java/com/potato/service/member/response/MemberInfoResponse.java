package com.potato.service.member.response;

import com.potato.domain.member.Member;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class MemberInfoResponse {

    private final Long id;

    private final String email;

    private final String nickname;

    private final String profileUrl;

    public static MemberInfoResponse of(Member member) {
        return new MemberInfoResponse(member.getId(), member.getEmail(), member.getNickname(), member.getProfileUrl());
    }
}
