package com.potato.domain.comment;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class BoardCommentCreator {

    public static BoardComment create(String content, Long memberId, Long boardId) {
        return BoardComment.newComment(content, memberId, boardId);
    }

}
