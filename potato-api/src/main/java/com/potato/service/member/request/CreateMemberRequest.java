package com.potato.service.member.request;

import com.potato.domain.member.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
public class CreateMemberRequest {

    @NotBlank
    private String email;

    @NotBlank
    private String name;

    private String profileUrl;

    @Builder(builderMethodName = "testBuilder")
    public CreateMemberRequest(@NotBlank String email, @NotBlank String name, String profileUrl) {
        this.email = email;
        this.name = name;
        this.profileUrl = profileUrl;
    }

    public Member toEntity() {
        return Member.newGoogleInstance(this.email, this.name, this.profileUrl);
    }

}
