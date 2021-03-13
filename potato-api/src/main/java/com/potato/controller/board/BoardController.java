package com.potato.controller.board;

import com.potato.config.argumentResolver.LoginMember;
import com.potato.config.session.MemberSession;
import com.potato.controller.ApiResponse;
import com.potato.service.board.BoardService;
import com.potato.service.board.dto.request.CreateBoardRequest;
import com.potato.service.board.dto.request.RetrieveLatestBoardListReqeust;
import com.potato.service.board.dto.request.UpdateBoardRequest;
import com.potato.service.board.dto.response.BoardInfoResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class BoardController {

    private final BoardService boardService;

    @Operation(summary = "게시글을 생성합니다.", description = "Bearer 토큰이 필요합니다.")
    @PostMapping("/board")
    public ApiResponse<BoardInfoResponse> createBoard(@Valid @RequestBody CreateBoardRequest request, @LoginMember MemberSession memberSession) {
        return ApiResponse.of(boardService.createBoard(request, memberSession.getMemberId()));
    }

    @GetMapping("/board/{boardId}")
    public ApiResponse<BoardInfoResponse> getBoard(@PathVariable Long boardId) {
        return ApiResponse.of(boardService.getBoard(boardId));
    }

    @Operation(summary = "게시글을 수정합니다.", description = "Bearer 토큰이 필요합니다.")
    @PutMapping("/board/{boardId}")
    public ApiResponse<BoardInfoResponse> updateBoard(@Valid @RequestBody UpdateBoardRequest request, @PathVariable Long boardId) {
        return ApiResponse.of(boardService.updateBoard(request, boardId));
    }

    @Operation(summary = "게시글을 좋아요합니다.", description = "Bearer 토큰이 필요합니다.")
    @PostMapping("/board/like/{boardId}")
    public ApiResponse<String> addBoardLike(@PathVariable Long boardId, @LoginMember MemberSession memberSession) {
        boardService.addBoardLike(boardId, memberSession.getMemberId());
        return ApiResponse.OK;
    }

    @Operation(summary = "게시글 좋아요를 취소합니다.", description = "Bearer 토큰이 필요합니다.")
    @DeleteMapping("/board/unlike/{boardId}")
    public ApiResponse<String> cancelBoardLike(@PathVariable Long boardId, @LoginMember MemberSession memberSession) {
        boardService.cancelBoardLike(boardId, memberSession.getMemberId());
        return ApiResponse.OK;
    }

    @Operation(summary = "그룹 리스트를 스크롤 방식으로 조회하는 API",
        description = "lastBoardId= 가장 마지막에 보여지는 게시물의 id, size = 몇개를 받아 올 것인가")
    @GetMapping("/board/list")
    public ApiResponse<List<BoardInfoResponse>> retrieveLatestBoardList(@Valid RetrieveLatestBoardListReqeust request) {
        return ApiResponse.of(boardService.retrieveLatestBoardList(request.getLastBoardId(), request.getSize()));

    }

}
