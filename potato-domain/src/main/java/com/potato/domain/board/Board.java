package com.potato.domain.board;

import com.potato.domain.BaseTimeEntity;
import com.potato.domain.Image;
import com.potato.domain.like.BoardLike;
import com.potato.exception.ConflictException;
import com.potato.exception.NotFoundException;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Board extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private Long memberId;

    private Long retweetId;

    @OneToMany(mappedBy = "board")
    private List<Image> images = new ArrayList<>();

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BoardLike> boardLikeList = new ArrayList<>();

    private int likesCount;

    @Builder
    public Board(String content, Long memberId, Long retweetId) {
        this.content = content;
        this.memberId = memberId;
        this.retweetId = retweetId;
        this.likesCount = 0;
    }

    public static Board newInstance(String content, Long memberId) {
        return Board.builder()
            .content(content)
            .memberId(memberId)
            .build();
    }

    public void update(String content) {
        this.content = content;
    }

    public void addLike(Long memberId) {
        if (hasAlreadyLike(memberId)) {
            throw new ConflictException(String.format("이미 멤버 (%s)는 게시물 (%s)에 좋아요를 눌렀습니다.", memberId, this.id));
        }
        BoardLike boardLike = BoardLike.of(this, memberId);
        this.boardLikeList.add(boardLike);
        this.likesCount++;
    }

    private boolean hasAlreadyLike(Long memberId) {
        return this.boardLikeList.stream()
            .anyMatch(boardLike -> boardLike.isSameEntity(memberId));
    }

    public void cancelLike(Long memberId) {
        BoardLike boardLike = findLike(memberId);
        boardLikeList.remove(boardLike);
        this.likesCount--;
    }

    private BoardLike findLike(Long memberId) {
        return this.boardLikeList.stream()
            .filter(mapper -> mapper.isSameEntity(memberId))
            .findFirst()
            .orElseThrow(() -> new NotFoundException(String.format("멤버 (%s)는 게시물 (%s)에 좋아요를 누른적이 없습니다.", memberId, this)));
    }

}
