package com.potato.domain.member;

import com.potato.domain.BaseTimeEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class Member extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;

    private String nickname;

    private String profileUrl;

    @Enumerated(EnumType.STRING)
    private MemberProvider provider;

    @Builder
    public Member(String email, String nickname, String profileUrl, MemberProvider provider) {
        this.email = email;
        this.nickname = nickname;
        this.profileUrl = profileUrl;
        this.provider = provider;
    }

    public static Member newGoogleInstance(String email, String nickname, String profileUrl) {
        return Member.builder()
            .email(email)
            .nickname(nickname)
            .profileUrl(profileUrl)
            .provider(MemberProvider.GOOGLE)
            .build();
    }
}
