package com.potato.service.comment;

import com.potato.domain.board.Board;
import com.potato.domain.board.repository.BoardRepository;
import com.potato.domain.comment.BoardCommentRepository;
import com.potato.domain.comment.BoardComment;
import com.potato.service.board.BoardServiceUtils;
import com.potato.service.comment.dto.request.AddCommentRequest;
import com.potato.service.comment.dto.response.BoardCommentResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class BoardCommentService {

    private final BoardCommentRepository boardCommentRepository;
    private final BoardRepository boardRepository;

    public void addComment(AddCommentRequest request, Long boardId, Long memberId) {
        BoardServiceUtils.findBoardById(boardRepository, boardId);
        boardCommentRepository.save(BoardComment.newComment(request.getContent(), memberId, boardId));
    }

    public List<BoardCommentResponse> getComment(Long boardId, Long memberId) {
        Board board = BoardServiceUtils.findBoardById(boardRepository, boardId);
        List<BoardComment> boardCommentList = boardCommentRepository.findBoardCommentById(board.getId());
        return boardCommentList.stream()
            .map(BoardCommentResponse::of)
            .collect(Collectors.toList());
    }
}
