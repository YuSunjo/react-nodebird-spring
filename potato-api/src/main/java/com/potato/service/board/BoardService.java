package com.potato.service.board;

import com.potato.domain.board.Board;
import com.potato.domain.board.repository.BoardRepository;
import com.potato.service.board.dto.request.CreateBoardRequest;
import com.potato.service.board.dto.request.UpdateBoardRequest;
import com.potato.service.board.dto.response.BoardInfoResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;

    @Transactional
    public BoardInfoResponse createBoard(CreateBoardRequest request, Long memberId) {
        Board board = boardRepository.save(request.toEntity(memberId));
        return BoardInfoResponse.of(board);
    }

    @Transactional(readOnly = true)
    public BoardInfoResponse getBoard(Long boardId) {
        Board board = BoardServiceUtils.findBoardById(boardRepository, boardId);
        return BoardInfoResponse.of(board);
    }

    @Transactional
    public BoardInfoResponse updateBoard(UpdateBoardRequest request, Long boardId) {
        Board board = BoardServiceUtils.findBoardById(boardRepository, boardId);
        board.update(request.getContent());
        return BoardInfoResponse.of(board);
    }

}
