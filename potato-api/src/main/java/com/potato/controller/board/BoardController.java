package com.potato.controller.board;

import com.potato.config.argumentResolver.LoginMember;
import com.potato.config.session.MemberSession;
import com.potato.controller.ApiResponse;
import com.potato.service.board.BoardService;
import com.potato.service.board.dto.request.CreateBoardRequest;
import com.potato.service.board.dto.response.BoardInfoResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

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

}
