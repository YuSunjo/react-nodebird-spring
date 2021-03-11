package com.potato.service.comment;

import com.potato.domain.board.repository.BoardRepository;
import com.potato.domain.comment.BoardComment;
import com.potato.domain.comment.BoardCommentRepository;
import com.potato.exception.NotFoundException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BoardCommentServiceUtils {

    public static BoardComment findBoardCommentByIdAndMemberId(BoardCommentRepository boardCommentRepository, Long boardCommentId, Long memberId) {
        BoardComment boardComment = boardCommentRepository.findBoardCommentByIdAndMemberId(boardCommentId, memberId);
        if (boardComment == null) {
            throw new NotFoundException("멤버가 작성한 댓글이 존재하지 않습니다.");
        }
        return boardComment;
    }

}
