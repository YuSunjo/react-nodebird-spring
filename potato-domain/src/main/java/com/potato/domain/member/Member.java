package com.potato.domain.member;

import com.potato.domain.BaseTimeEntity;
import com.potato.domain.follow.Follow;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

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

    @OneToMany(mappedBy = "following")
    private List<Follow> follows = new ArrayList<>();

    @OneToMany(mappedBy = "follower")
    private List<Follow> followers = new ArrayList<>();

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

    public void update(String nickname) {
        this.nickname = nickname;
    }
}
