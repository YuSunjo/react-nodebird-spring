package com.potato.service.auth.dto.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class AuthResponse {

    private final AuthType type;

    private final String email;

    private final String nickname;

    private final String token;

    public static AuthResponse signUp(String email, String nickname) {
        return new AuthResponse(AuthType.SIGN_UP, email, nickname, null);
    }

    public static AuthResponse login(String token) {
        return new AuthResponse(AuthType.LOGIN, null, null, token);
    }

    public enum AuthType {
        SIGN_UP,
        LOGIN
    }

}
