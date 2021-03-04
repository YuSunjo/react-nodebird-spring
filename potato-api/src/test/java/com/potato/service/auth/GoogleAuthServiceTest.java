package com.potato.service.auth;

import com.potato.domain.member.MemberCreator;
import com.potato.domain.member.MemberRepository;
import com.potato.external.google.GoogleApiCaller;
import com.potato.external.google.dto.response.GoogleAccessTokenResponse;
import com.potato.external.google.dto.response.GoogleUserInfoResponse;
import com.potato.service.auth.dto.request.AuthRequest;
import com.potato.service.auth.dto.response.AuthResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class GoogleAuthServiceTest {

    private GoogleAuthService googleAuthService;

    @Autowired
    private MemberRepository memberRepository;

    @AfterEach
    void cleanUp() {
        memberRepository.deleteAll();
    }

    @BeforeEach
    void setUpGoogleAuthService() {
        googleAuthService = new GoogleAuthService(new MockHttpSession(), new MockGoogleApiCaller(), memberRepository);
    }

    @Test
    void 구글_인증시_존재하지_않는_이메일의경우_회원가입을_위한_정보가_반환된다() {
        // given
        AuthRequest request = AuthRequest.testBuilder()
            .code("code")
            .redirectUri("redirectUri")
            .build();

        // when
        AuthResponse response = googleAuthService.handleGoogleAuthentication(request);

        // then
        assertThat(response.getType()).isEqualTo(AuthResponse.AuthType.SIGN_UP);
        assertThat(response.getEmail()).isEqualTo("tnswh2023@naver.com");
        assertThat(response.getNickname()).isEqualTo("유순조");
        assertThat(response.getToken()).isNull();
    }

    @Test
    void 구_인증시_이미_존재하는_이메일의경우_로그인이_진행된다() {
        // given
        memberRepository.save(MemberCreator.create("tnswh2023@naver.com"));

        AuthRequest request = AuthRequest.testBuilder()
            .code("code")
            .redirectUri("redirectUri")
            .build();

        // when
        AuthResponse response = googleAuthService.handleGoogleAuthentication(request);

        // then
        assertThat(response.getType()).isEqualTo(AuthResponse.AuthType.LOGIN);
        assertThat(response.getEmail()).isNull();
        assertThat(response.getNickname()).isNull();
    }

    private static class MockGoogleApiCaller implements GoogleApiCaller {

        @Override
        public GoogleAccessTokenResponse getGoogleAccessToken(String code, String redirectUri) {
            return GoogleAccessTokenResponse.testBuilder()
                .accessToken("accessToken")
                .build();
        }

        @Override
        public GoogleUserInfoResponse getGoogleUserProfileInfo(String accessToken) {
            return GoogleUserInfoResponse.testBuilder()
                .email("tnswh2023@naver.com")
                .name("유순조")
                .build();
        }

    }

}
