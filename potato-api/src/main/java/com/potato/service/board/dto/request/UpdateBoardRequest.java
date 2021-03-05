package com.potato.service.board.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
public class UpdateBoardRequest {

    @NotBlank
    private String content;

    public UpdateBoardRequest(String content) {
        this.content = content;
    }

    public static UpdateBoardRequest testInstance(String content) {
        return new UpdateBoardRequest(content);
    }

}
