package com.potato.domain.member;

import com.potato.domain.BaseTimeEntity;
import com.potato.domain.follow.Follow;
import com.potato.exception.NotFoundException;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

    private int followerCount;

    @Enumerated(EnumType.STRING)
    private MemberProvider provider;

    @OneToMany(mappedBy = "follower", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Follow> followerList = new ArrayList<>();  // 특정 대상을 팔로우 하다.

    @Builder
    public Member(String email, String nickname, String profileUrl, MemberProvider provider) {
        this.email = email;
        this.nickname = nickname;
        this.profileUrl = profileUrl;
        this.provider = provider;
        this.followerCount = 0;
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

    public void addFollowing(Long memberId) {
        this.followerList.add(Follow.newFollow(memberId, this));
        System.out.println("memberId = " + memberId);
        followerCount++;
    }

    public List<Long> getFollowerId() {
        return this.followerList.stream()
            .map(Follow::getMemberId)
            .collect(Collectors.toList());
    }

    public void unFollowing(Member targetMember, Long memberId) {
        Follow followerMember = this.findFollowerMember(memberId, targetMember);
        followerList.remove(followerMember);
        followerCount--;
    }

    private Follow findFollowerMember(Long memberId, Member targetMember) {
        return this.followerList.stream()
            .filter(mapper -> mapper.isSameMember(memberId))
            .findFirst()
            .orElseThrow(() -> new NotFoundException(String.format("해당하는 멤버(%s)에 팔로우한 멤버(%s)가 없습니다.", targetMember, memberId)));
    }

}
