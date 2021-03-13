package com.potato.service.board;

import com.potato.domain.board.Board;
import com.potato.domain.board.repository.BoardRepository;
import com.potato.service.board.dto.request.CreateBoardRequest;
import com.potato.service.board.dto.request.RetrieveLatestBoardListReqeust;
import com.potato.service.board.dto.request.UpdateBoardRequest;
import com.potato.service.board.dto.response.BoardInfoResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

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

    @Transactional
    public void addBoardLike(Long boardId, Long memberId) {
        Board board = BoardServiceUtils.findBoardById(boardRepository, boardId);
        board.addLike(memberId);
    }

    @Transactional
    public void cancelBoardLike(Long boardId, Long memberId) {
        Board board = BoardServiceUtils.findBoardById(boardRepository, boardId);
        board.cancelLike(memberId);
    }

    @Transactional
    public List<BoardInfoResponse> retrieveLatestBoardList(long lastBoardId, int size) {
        return lastBoardId == 0 ? getLatestBoards(size) : getLatestBoardLessThanId(lastBoardId, size);
    }

    private List<BoardInfoResponse> getLatestBoards(int size) {
        return boardRepository.findBoardsOrderByDesc(size).stream()
            .map(BoardInfoResponse::of).collect(Collectors.toList());
    }

    private List<BoardInfoResponse> getLatestBoardLessThanId(long lastBoardId, int size) {
        return boardRepository.findBoardsLessThanOrderByIdDescLimit(lastBoardId, size)
            .stream()
            .map(BoardInfoResponse::of)
            .collect(Collectors.toList());
    }

}
