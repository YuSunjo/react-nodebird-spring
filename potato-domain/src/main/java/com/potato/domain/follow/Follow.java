package com.potato.domain.follow;

import com.potato.domain.BaseTimeEntity;
import com.potato.domain.member.Member;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Follow extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "following_id")
    private Member following;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "follower_id")
    private Member follower;

    private Follow(Member following, Member follower) {
        this.following = following;
        this.follower = follower;
    }

    public static Follow newFollow(Member following, Member follower) {
        return new Follow(following, follower);
    }

}
