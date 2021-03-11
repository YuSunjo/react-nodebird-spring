package com.potato.service.comment.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
public class AddCommentRequest {

    @NotBlank
    private String content;

    public AddCommentRequest(@NotBlank String content) {
        this.content = content;
    }

    public static AddCommentRequest testInstance(String content) {
        return new AddCommentRequest(content);
    }
}
