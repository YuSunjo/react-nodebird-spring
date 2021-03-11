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

    @Column(nullable = false)
    private Long memberId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "follower_id")
    private Member follower;

    private Follow(Long memberId, Member follower) {
        this.memberId = memberId;
        this.follower = follower;
    }

    public static Follow newFollow(Long memberId, Member follower) {
        return new Follow(memberId, follower);
    }

    public boolean isSameMember(Long memberId) {
        return this.memberId.equals(memberId);
    }
}
