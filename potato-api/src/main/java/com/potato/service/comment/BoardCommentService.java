package com.potato.service.comment;

import com.potato.domain.board.repository.BoardRepository;
import com.potato.domain.comment.BoardCommentRepository;
import com.potato.domain.comment.BoardComment;
import com.potato.service.board.BoardServiceUtils;
import com.potato.service.comment.dto.request.AddCommentRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class BoardCommentService {

    private final BoardCommentRepository boardCommentRepository;
    private final BoardRepository boardRepository;

    public void addComment(AddCommentRequest request, Long boardId, Long memberId) {
        BoardServiceUtils.findBoardById(boardRepository, boardId);
        boardCommentRepository.save(BoardComment.newComment(request.getContent(), memberId, boardId));
    }
}
