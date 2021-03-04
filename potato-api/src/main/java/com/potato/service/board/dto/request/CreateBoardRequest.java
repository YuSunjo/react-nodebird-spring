package com.potato.service.board.dto.request;

import com.potato.domain.board.Board;
import lombok.Builder;

import javax.validation.constraints.NotBlank;

public class CreateBoardRequest {

    @NotBlank
    private String content;

    @Builder(builderMethodName = "testBuilder")
    public CreateBoardRequest(@NotBlank String content) {
        this.content = content;
    }

    public Board toEntity(Long memberId) {
        return Board.newInstance(content, memberId);
    }

}
