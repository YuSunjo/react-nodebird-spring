package com.potato.domain.board.repository;

import com.potato.domain.board.Board;

import java.util.List;

public interface BoardRepositoryCustom {

    Board findBoardById(Long BoardId);

    List<Board> findBoardsOrderByDesc(int size);

    List<Board> findBoardsLessThanOrderByIdDescLimit(long lastBoardId, int size);
}
