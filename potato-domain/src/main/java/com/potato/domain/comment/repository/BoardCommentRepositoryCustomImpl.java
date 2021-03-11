package com.potato.domain.comment.repository;

import com.potato.domain.comment.BoardComment;
import com.potato.domain.comment.BoardCommentRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.potato.domain.comment.QBoardComment.boardComment;

@RequiredArgsConstructor
public class BoardCommentRepositoryCustomImpl implements BoardCommentRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<BoardComment> findBoardCommentById(Long boardId) {
        return queryFactory.selectFrom(boardComment)
            .where(
                boardComment.boardId.eq(boardId)
            ).fetch();
    }
}
