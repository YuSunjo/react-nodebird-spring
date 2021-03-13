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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.Collections;
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

    @Test
    void 가장_최신의_게시물_3개를_불러온다() {
        //given
        Board board1 = BoardCreator.create("content1", memberId);
        Board board2 = BoardCreator.create("content2", memberId);
        Board board3 = BoardCreator.create("content3", memberId);

        boardRepository.saveAll(Arrays.asList(board1, board2, board3));

        //when
        List<BoardInfoResponse> responses = boardService.retrieveLatestBoardList(0, 3);

        //then
        assertThat(responses).hasSize(3);
        assertThat(responses.get(0).getContent()).isEqualTo("content3");
    }

    @Test
    void 가장_최시니의_게시물_3개를_불러올때_게시글이_없을_경우_빈리스트_반환() {
        //given
        List<BoardInfoResponse> responses = boardService.retrieveLatestBoardList(0, 3);

        //then
        assertThat(responses).isEmpty();
    }

    @DisplayName("게시물 4 이후로부터 3개를 조회했으니 [3, 2, 1]가 조회되어야 한다.")
    @Test
    void 게시물_스크롤_페이지네이션_조회_기능_1() {
        //given
        Board board1 = BoardCreator.create("content1", memberId);
        Board board2 = BoardCreator.create("content2", memberId);
        Board board3 = BoardCreator.create("content3", memberId);
        Board board4 = BoardCreator.create("content4", memberId);
        Board board5 = BoardCreator.create("content5", memberId);

        boardRepository.saveAll(Arrays.asList(board1, board2, board3, board4, board5));

        //when
        List<BoardInfoResponse> responses = boardService.retrieveLatestBoardList(board4.getId(), 3);

        //then
        assertThat(responses.size()).isEqualTo(3);
        assertThat(responses.get(0).getContent()).isEqualTo(board3.getContent());
        assertThat(responses.get(1).getContent()).isEqualTo(board2.getContent());
        assertThat(responses.get(2).getContent()).isEqualTo(board1.getContent());
    }

    @DisplayName("게시물 5 이후로부터 3개를 조회했으니 [4, 3, 2]가 조회되어야 한다.")
    @Test
    void 게시물_스크롤_페이지네이션_조회_기능_2() {
        //given
        Board board1 = BoardCreator.create("content1", memberId);
        Board board2 = BoardCreator.create("content2", memberId);
        Board board3 = BoardCreator.create("content3", memberId);
        Board board4 = BoardCreator.create("content4", memberId);
        Board board5 = BoardCreator.create("content5", memberId);

        boardRepository.saveAll(Arrays.asList(board1, board2, board3, board4, board5));

        //when
        List<BoardInfoResponse> responses = boardService.retrieveLatestBoardList(board5.getId(), 3);

        //then
        assertThat(responses.size()).isEqualTo(3);
        assertThat(responses.get(0).getContent()).isEqualTo(board4.getContent());
        assertThat(responses.get(1).getContent()).isEqualTo(board3.getContent());
        assertThat(responses.get(2).getContent()).isEqualTo(board2.getContent());
    }

    @DisplayName("게시물 5 이후로부터 2개를 조회했으니 [4, 3]가 조회되어야 한다.")
    @Test
    void 게시물_스크롤_페이지네이션_조회_기능_3() {
        //given
        Board board1 = BoardCreator.create("content1", memberId);
        Board board2 = BoardCreator.create("content2", memberId);
        Board board3 = BoardCreator.create("content3", memberId);
        Board board4 = BoardCreator.create("content4", memberId);
        Board board5 = BoardCreator.create("content5", memberId);

        boardRepository.saveAll(Arrays.asList(board1, board2, board3, board4, board5));

        //when
        List<BoardInfoResponse> responses = boardService.retrieveLatestBoardList(board5.getId(), 2);

        //then
        assertThat(responses.size()).isEqualTo(2);
        assertThat(responses.get(0).getContent()).isEqualTo(board4.getContent());
        assertThat(responses.get(1).getContent()).isEqualTo(board3.getContent());
    }

    @Test
    void 게시물_스크롤_페이지네이션_조회_기능이_더이상_게시물이_존재하지_않을경우() {
        //given
        Board board = BoardCreator.create("content1", memberId);

        boardRepository.save(board);
//        boardRepository.saveAll(Collections.singleton(board));

        //when
        List<BoardInfoResponse> responses = boardService.retrieveLatestBoardList(board.getId(), 3);

        //then
        assertThat(responses).isEmpty();
    }

}
