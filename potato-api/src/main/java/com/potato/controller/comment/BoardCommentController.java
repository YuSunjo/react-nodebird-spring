package com.potato.controller.comment;


import com.potato.config.argumentResolver.LoginMember;
import com.potato.config.session.MemberSession;
import com.potato.controller.ApiResponse;
import com.potato.service.comment.dto.request.AddCommentRequest;
import com.potato.service.comment.BoardCommentService;
import com.potato.service.comment.dto.response.BoardCommentResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class BoardCommentController {

    private final BoardCommentService boardCommentService;

    @Operation(summary = "게시물에 댓글 추가", description = "Bearer 토큰이 필요합니다.")
    @PostMapping("/board/{boardId}/comment")
    public ApiResponse<String> addComment(@RequestBody AddCommentRequest request, @PathVariable Long boardId, @LoginMember MemberSession memberSession) {
        boardCommentService.addComment(request, boardId, memberSession.getMemberId());
        return ApiResponse.OK;
    }

    @Operation(summary = "게시물 댓글 가져오기", description = "Bearer 토큰이 필요합니다.")
    @GetMapping("/board/{boardId}/comment")
    public ApiResponse<List<BoardCommentResponse>> getBoardComment(@PathVariable Long boardId, @LoginMember MemberSession memberSession) {
        return ApiResponse.of(boardCommentService.getComment(boardId, memberSession.getMemberId()));
    }

    @Operation(summary = "게시물 댓글 삭제하기", description = "Bearer 토큰이 필요합니다.")
    @DeleteMapping("/board/comment")
    public ApiResponse<String> deleteBoardComment(@RequestParam Long boardCommentId, @LoginMember MemberSession memberSession) {
        boardCommentService.deleteBoardComment(boardCommentId, memberSession.getMemberId());
        return ApiResponse.OK;
    }

}
