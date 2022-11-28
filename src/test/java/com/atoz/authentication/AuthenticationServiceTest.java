package com.atoz.authentication;

import com.atoz.authentication.help.StubAuthenticationManager;
import com.atoz.authentication.help.StubAuthorizationProvider;
import com.atoz.authentication.help.StubRefreshTokenMapper;
import com.atoz.authentication.help.StubTokenProvider;
import com.atoz.security.authentication.AuthenticationService;
import com.atoz.security.authentication.AuthenticationServiceImpl;
import com.atoz.security.authentication.dto.TokenRequestDTO;
import com.atoz.security.token.RefreshTokenEntity;
import com.atoz.security.authentication.dto.TokenResponseDTO;
import com.atoz.security.authentication.dto.SigninDTO;
import com.atoz.security.token.RefreshTokenMapper;
import com.atoz.user.UserMapper;
import com.atoz.user.entity.Authority;
import com.atoz.user.entity.UserEntity;
import com.atoz.user.help.SpyUserMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class AuthenticationServiceTest {

    private final UserMapper userMapper = new SpyUserMapper();
    private final RefreshTokenMapper refreshTokenMapper = new StubRefreshTokenMapper();
    private final AuthenticationService authService = new AuthenticationServiceImpl(
            new StubAuthenticationManager(),
            userMapper,
            refreshTokenMapper,
            new StubTokenProvider(),
            new StubAuthorizationProvider());

    private UserEntity signedUpUser = UserEntity.builder()
            .userId("testUserId")
            .password("testPassword")
            .nickname("testNickname")
            .email("test@test.com")
            .authorities(Set.of(Authority.ROLE_USER))
            .build();

    @BeforeEach
    private void setUp() {
        userMapper.addUser(signedUpUser);
    }

    /**
     * 로그인 실패는 AuthenticationManager에서 예외를 던져서 처리하므로
     * 해당 클래스에 책임이 없기에 토큰이 잘 발급되는지만 확인합니다.
     */
    @Test
    void signin_로그인하면_토큰이_발급되어야한다() {
        SigninDTO presentedIdPassword = SigninDTO.builder()
                .userId(signedUpUser.getUserId())
                .password(signedUpUser.getPassword())
                .build();


        TokenResponseDTO providedToken = authService.signin(presentedIdPassword);


        assertThat(providedToken).isNotNull();
        assertThat(providedToken.getAccessToken()).isNotBlank();
        assertThat(providedToken.getRefreshToken()).isNotBlank();
    }

    @Test
    void signout_로그아웃하면_리프레시토큰이_삭제되어야한다() {
        SigninDTO presentedIdPassword = SigninDTO.builder()
                .userId(signedUpUser.getUserId())
                .password(signedUpUser.getPassword())
                .build();
        TokenResponseDTO signedInUser = authService.signin(presentedIdPassword);
        TokenRequestDTO signoutRequestDTO = new TokenRequestDTO(signedInUser.getAccessToken(), signedInUser.getRefreshToken());


        authService.signout(signoutRequestDTO);


        RefreshTokenEntity foundToken = refreshTokenMapper.findTokenByKey(presentedIdPassword.getUserId()).orElse(null);
        assertThat(foundToken).isNull();
    }

    @Test
    void refresh_토큰을_재발급_받을수있다() throws InterruptedException {
        SigninDTO presentedIdPassword = SigninDTO.builder()
                .userId(signedUpUser.getUserId())
                .password(signedUpUser.getPassword())
                .build();
        TokenResponseDTO signedInUser = authService.signin(presentedIdPassword);
        TokenRequestDTO refreshRequestDTO = new TokenRequestDTO(signedInUser.getAccessToken(), signedInUser.getRefreshToken());


        TokenResponseDTO reissuedToken = authService.refresh(refreshRequestDTO);


        assertThat(reissuedToken).isNotNull();
        assertThat(reissuedToken.getAccessToken()).isNotBlank();
        assertThat(reissuedToken.getRefreshToken()).isNotBlank();
    }
}