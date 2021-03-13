package com.potato.domain.board.repository;

import com.potato.domain.board.Board;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jdk.jfr.Frequency;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.potato.domain.board.QBoard.board;

@RequiredArgsConstructor
public class BoardRepositoryCustomImpl implements BoardRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Board findBoardById(Long boardId) {
        return queryFactory.selectFrom(board)
            .where(
                board.id.eq(boardId)
            )
            .fetchOne();
    }

    @Override
    public List<Board> findBoardsOrderByDesc(int size) {
        return queryFactory.selectFrom(board)
            .orderBy(board.id.desc())
            .limit(size)
            .fetch();
    }

    @Override
    public List<Board> findBoardsLessThanOrderByIdDescLimit(long lastBoardId, int size) {
        return queryFactory.selectFrom(board)
            .where(
                board.id.lt(lastBoardId)
            )
            .orderBy(board.id.desc())
            .limit(size)
            .fetch();
    }

}
