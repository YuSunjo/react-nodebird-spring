package com.potato.domain.comment.repository;

import com.potato.domain.comment.BoardComment;
import com.potato.domain.comment.BoardCommentRepository;

import java.util.List;

public interface BoardCommentRepositoryCustom {

    List<BoardComment> findBoardCommentById(Long boardId);

}
