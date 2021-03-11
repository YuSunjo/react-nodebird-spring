package com.potato.service.comment;

import com.potato.domain.board.Board;
import com.potato.domain.board.BoardCreator;
import com.potato.domain.board.repository.BoardRepository;
import com.potato.domain.comment.BoardComment;
import com.potato.domain.comment.BoardCommentCreator;
import com.potato.domain.comment.BoardCommentRepository;
import com.potato.domain.member.MemberCreator;
import com.potato.service.MemberSetupTest;
import com.potato.service.comment.dto.request.AddCommentRequest;
import com.potato.service.comment.dto.response.BoardCommentResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
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

    Board board;

    @BeforeEach
    void setupBoard() {
        String boardContent = "boardContent입니다.";
        board = BoardCreator.create(boardContent, memberId);
        boardRepository.save(board);
    }

    @AfterEach
    void cleanUp() {
        super.cleanup();
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

    @Test
    void 댓글들을_가져옵니다() {
        //given
        String content1 = "content1";
        String content2 = "content2";

        BoardComment boardComment1 = BoardCommentCreator.create(content1, memberId, board.getId());
        BoardComment boardComment2 = BoardCommentCreator.create(content2, memberId, board.getId());
        boardCommentRepository.saveAll(Arrays.asList(boardComment1, boardComment2));

        //when
        List<BoardCommentResponse> responses = boardCommentService.getComment(board.getId(), memberId);

        //then
        assertThat(responses.get(0).getContent()).isEqualTo(content1);
        assertThat(responses.get(1).getContent()).isEqualTo(content2);
    }

}
