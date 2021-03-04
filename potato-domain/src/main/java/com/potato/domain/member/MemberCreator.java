package com.potato.domain.member;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class MemberCreator {

    public static Member create(String email) {
        return Member.builder()
            .email(email)
            .nickname("멤버")
            .provider(MemberProvider.GOOGLE)
            .build();
    }

    public static Member create(String email, String nickname, String profileUrl) {
        return Member.builder()
            .email(email)
            .nickname(nickname)
            .profileUrl(profileUrl)
            .provider(MemberProvider.GOOGLE)
            .build();
    }

}
