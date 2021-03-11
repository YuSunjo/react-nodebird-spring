package com.potato.service.comment;

import com.potato.domain.board.Board;
import com.potato.domain.board.BoardCreator;
import com.potato.domain.board.repository.BoardRepository;
import com.potato.domain.comment.BoardComment;
import com.potato.domain.comment.BoardCommentRepository;
import com.potato.domain.member.MemberCreator;
import com.potato.service.MemberSetupTest;
import com.potato.service.comment.dto.request.AddCommentRequest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class BoardCommentServiceTest extends MemberSetupTest {

    @Autowired
    BoardCommentService boardCommentService;

    @Autowired
    BoardCommentRepository boardCommentRepository;

    @Autowired
    BoardRepository boardRepository;

    @AfterEach
    void cleanUp() {
        boardCommentRepository.deleteAll();
        boardRepository.deleteAll();
    }

    @Test
    void 댓글을_생성합니다() {
        //given
        String boardContent = "boardContent입니다.";
        Board board = BoardCreator.create(boardContent, memberId);
        boardRepository.save(board);

        String content = "content입니다.";
        AddCommentRequest request = AddCommentRequest.testInstance(content);

        //when
        boardCommentService.addComment(request, board.getId(), memberId);

        //then
        List<BoardComment> boardCommentList = boardCommentRepository.findAll();
        assertThat(boardCommentList.get(0).getContent()).isEqualTo(content);
    }

}
