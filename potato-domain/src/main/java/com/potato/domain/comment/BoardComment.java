package com.potato.domain.comment;

import com.potato.domain.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class BoardComment extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private Long memberId;

    @Column(nullable = false)
    private Long boardId;

    private boolean isDeleted;

    private BoardComment(String content, Long memberId, Long boardId) {
        this.content = content;
        this.memberId = memberId;
        this.boardId = boardId;
        this.isDeleted = false;
    }

    public static BoardComment newComment(String content, Long memberId, Long boardId) {
        return new BoardComment(content, memberId, boardId);
    }

}
