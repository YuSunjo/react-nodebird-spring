package com.potato.controller.comment;


import com.potato.config.argumentResolver.LoginMember;
import com.potato.config.session.MemberSession;
import com.potato.controller.ApiResponse;
import com.potato.service.comment.dto.request.AddCommentRequest;
import com.potato.service.comment.BoardCommentService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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

}
