package com.potato.service.board;

import com.potato.domain.board.Board;
import com.potato.domain.board.BoardCreator;
import com.potato.domain.board.repository.BoardRepository;
import com.potato.domain.like.BoardLike;
import com.potato.domain.like.BoardLikeRepository;
import com.potato.domain.member.MemberRepository;
import com.potato.exception.ConflictException;
import com.potato.exception.NotFoundException;
import com.potato.service.MemberSetupTest;
import com.potato.service.board.dto.request.CreateBoardRequest;
import com.potato.service.board.dto.request.UpdateBoardRequest;
import com.potato.service.board.dto.response.BoardInfoResponse;
import com.potato.service.member.MemberService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
public class BoardServiceTest extends MemberSetupTest {

    @Autowired
    private BoardService boardService;

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private BoardLikeRepository boardLikeRepository;

    @AfterEach
    void cleanUp() {
        super.cleanup();
        boardRepository.deleteAll();
    }

    @Test
    public void 게시글을_생성한다() {
        //given
        String content = "content";
        CreateBoardRequest request = CreateBoardRequest.testBuilder()
            .content(content)
            .build();

        //when
        boardService.createBoard(request, memberId);

        //then
        List<Board> boardList = boardRepository.findAll();
        assertThat(boardList).hasSize(1);
        assertThat(boardList.get(0).getContent()).isEqualTo(content);
    }

    @Test
    public void 특정게시글을_가져온다() {
        //given
        String content = "content";
        Board board = BoardCreator.create(content, memberId);

        boardRepository.save(board);

        //when
        BoardInfoResponse response = boardService.getBoard(board.getId());

        //then
        assertThat(response.getContent()).isEqualTo(content);
    }

    @Test
    public void 게시글을_수정한다() {
        //given
        String content = "updateContent";
        UpdateBoardRequest request = UpdateBoardRequest.testInstance(content);

        Board board = BoardCreator.create("content", memberId);
        boardRepository.save(board);

        //when
        System.out.println("board.getId() = " + board.getId());
        System.out.println(board.getContent());
        BoardInfoResponse response = boardService.updateBoard(request, board.getId());

        //then
        assertThat(response.getContent()).isEqualTo(content);
    }

    @Test
    void 게시물을_좋아요한다() {
        //given
        Board board = BoardCreator.create("content입니다.", memberId);
        boardRepository.save(board);

        //when
        boardService.addBoardLike(board.getId(), 100L);

        //then
        List<Board> boardList = boardRepository.findAll();
        assertThat(boardList).hasSize(1);
        assertThat(boardList.get(0).getLikesCount()).isEqualTo(1);

        List<BoardLike> boardLikeList = boardLikeRepository.findAll();
        assertThat(boardLikeList).hasSize(1);
        assertThat(boardLikeList.get(0).getMemberId()).isEqualTo(100L);
    }

    @Test
    void 이미_좋아요를_누른_상태일_경우_컨플릭트_애러뜬다() {
        //given
        Board board = BoardCreator.create("content입니다.", memberId);
        board.addLike(100L);
        boardRepository.save(board);
        //when & then
        assertThatThrownBy(
            () -> boardService.addBoardLike(board.getId(), 100L)
        ).isInstanceOf(ConflictException.class);
    }

    @Test
    void 좋아요를_취소한다() {
        //given
        Board board = BoardCreator.create("content입니다.", memberId);
        board.addLike(100L);
        boardRepository.save(board);

        //when
        boardService.cancelBoardLike(board.getId(), 100L);

        //then
        List<Board> boardList = boardRepository.findAll();
        assertThat(boardList).hasSize(1);
        assertThat(boardList.get(0).getLikesCount()).isEqualTo(0);

        List<BoardLike> boardLikeList = boardLikeRepository.findAll();
        assertThat(boardLikeList).isEmpty();
    }

    @Test
    void 게시물에_누르지_않은_좋아요를_취소하면_애러발생() {
        //given
        Board board = BoardCreator.create("content입니다.", memberId);
        boardRepository.save(board);

        //when & then
        assertThatThrownBy(
            () -> boardService.cancelBoardLike(board.getId(), memberId)
        ).isInstanceOf(NotFoundException.class);
    }

}
