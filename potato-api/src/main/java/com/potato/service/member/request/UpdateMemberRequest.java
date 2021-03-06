package com.potato.service.member.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.sql.Update;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
public class UpdateMemberRequest {

    @NotBlank
    private String nickname;

    public UpdateMemberRequest(@NotBlank String nickname) {
        this.nickname = nickname;
    }

    public static UpdateMemberRequest testInstance(String nickname) {
        return new UpdateMemberRequest(nickname);
    }

}
