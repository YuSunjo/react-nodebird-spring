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

    private String name;

    private String profileUrl;

    @Enumerated(EnumType.STRING)
    private MemberProvider provider;

    @Builder
    public Member(String email, String name, String profileUrl, MemberProvider provider) {
        this.email = email;
        this.name = name;
        this.profileUrl = profileUrl;
        this.provider = provider;
    }

}