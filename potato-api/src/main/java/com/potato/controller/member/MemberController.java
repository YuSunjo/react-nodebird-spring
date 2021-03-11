package com.potato.controller.member;

import com.potato.config.argumentResolver.LoginMember;
import com.potato.config.session.MemberSession;
import com.potato.controller.ApiResponse;
import com.potato.service.member.MemberService;
import com.potato.service.member.request.CreateMemberRequest;
import com.potato.service.member.request.UpdateMemberRequest;
import com.potato.service.member.response.MemberInfoResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import java.util.List;

import static com.potato.config.session.SessionConstants.AUTH_SESSION;

@RequiredArgsConstructor
@RestController
public class MemberController {

    private final HttpSession httpSession;
    private final MemberService memberService;

    @PostMapping("/api/v1/member")
    public ApiResponse<String> createMember(@Valid @RequestBody CreateMemberRequest request) {
        Long memberId = memberService.createMember(request);
        httpSession.setAttribute(AUTH_SESSION, MemberSession.of(memberId));
        return ApiResponse.of(httpSession.getId());
    }

    @Operation(summary = "내 정보를 불러오는 API", description = "Bearer 토큰이 필요합니다.")
    @GetMapping("/api/v1/member")
    public ApiResponse<MemberInfoResponse> getMyMemberInfo(@LoginMember MemberSession memberSession) {
        return ApiResponse.of(memberService.getMemberInfo(memberSession.getMemberId()));
    }

    @Operation(summary = "내 정보를 수정하는 API", description = "Bearer 토큰이 필요합니다.")
    @PatchMapping("/member")
    public ApiResponse<MemberInfoResponse> updateMemberInfo(@Valid @RequestBody UpdateMemberRequest request,
                                                            @LoginMember MemberSession memberSession) {
        return ApiResponse.of(memberService.updateMemberInfo(request, memberSession.getMemberId()));
    }

    @Operation(summary = "팔로우 유저", description = "Bearer 토큰이 필요합니다.")
    @PostMapping("/follow/{targetId}")
    public ApiResponse<String> followMember(@PathVariable Long targetId, @LoginMember MemberSession memberSession) {
        memberService.followMember(targetId, memberSession.getMemberId());
        return ApiResponse.OK;
    }

    @Operation(summary = "팔로우한 유저를 가져옵니다.", description = "Bearer 토큰이 필요합니다.")
    @GetMapping("/follower")
    public ApiResponse<List<MemberInfoResponse>> getFollowerMember(@LoginMember MemberSession memberSession) {
        return ApiResponse.of(memberService.getFollowerMember(memberSession.getMemberId()));
    }

    @Operation(summary = "나를 팔로우한 유저를 가져옵니다.", description = "Bearer 토큰이 필요합니다.")
    @GetMapping("/toMeFollower")
    public ApiResponse<List<MemberInfoResponse>> getToMeFollowerMember(@LoginMember MemberSession memberSession) {
        return ApiResponse.of(memberService.getToMeFollowerMember(memberSession.getMemberId()));
    }

    @Operation(summary = "내가 팔로우한 유저 취소하기", description = "Bearer 토큰이 필요합니다.")
    @DeleteMapping("/api/v1/organization/follow/{targetId}")
    public ApiResponse<String> unFollowMember(@PathVariable Long targetId, @LoginMember MemberSession memberSession) {
        memberService.unFollowMember(targetId, memberSession.getMemberId());
        return ApiResponse.OK;
    }

}
