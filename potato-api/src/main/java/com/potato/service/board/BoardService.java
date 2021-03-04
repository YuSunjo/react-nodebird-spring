package com.potato.service.board;

import com.potato.domain.board.Board;
import com.potato.domain.board.repository.BoardRepository;
import com.potato.service.board.dto.request.CreateBoardRequest;
import com.potato.service.board.dto.response.BoardInfoResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;

    public BoardInfoResponse createBoard(CreateBoardRequest request, Long memberId) {
        Board board = boardRepository.save(request.toEntity(memberId));
        return BoardInfoResponse.of(board);
    }

    public BoardInfoResponse getBoard(Long boardId) {
        Board board = BoardServiceUtils.findBoardById(boardRepository, boardId);
        return BoardInfoResponse.of(board);
    }
}
