package com.potato.service.comment.dto.response;

import com.potato.domain.comment.BoardComment;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class BoardCommentResponse {

    private final Long id;

    private final String content;

    private final Long memberId;

    public static BoardCommentResponse of(BoardComment boardComment) {
        return new BoardCommentResponse(boardComment.getId(), boardComment.getContent(), boardComment.getMemberId());
    }

}
