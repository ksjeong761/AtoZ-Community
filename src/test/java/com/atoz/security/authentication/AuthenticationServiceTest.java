package com.atoz.security.authentication;

import com.atoz.security.authentication.dto.request.SignoutRequestDto;
import com.atoz.security.authentication.helper.StubAuthenticationManager;
import com.atoz.security.token.helper.MockRefreshTokenMapper;
import com.atoz.security.token.helper.StubTokenManager;
import com.atoz.security.authentication.dto.request.TokenRequestDto;
import com.atoz.security.token.dto.RefreshTokenDto;
import com.atoz.security.authentication.dto.response.TokenResponseDto;
import com.atoz.security.authentication.dto.request.SigninRequestDto;
import com.atoz.security.token.RefreshTokenMapper;
import com.atoz.user.UserMapper;
import com.atoz.user.Authority;
import com.atoz.user.dto.UserDto;
import com.atoz.user.helper.SpyUserMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class AuthenticationServiceTest {

    private final UserMapper userMapper = new SpyUserMapper();
    private final RefreshTokenMapper refreshTokenMapper = new MockRefreshTokenMapper();
    private AuthenticationService sut;

    private UserDto signedUpUser;

    @BeforeEach
    private void setUp() {
        signedUpUser = UserDto.builder()
                .userId("testUserId")
                .password("testPassword")
                .nickname("testNickname")
                .email("test@test.com")
                .authorities(Set.of(Authority.ROLE_USER))
                .build();
        userMapper.addUser(signedUpUser);

        sut = new AuthenticationServiceImpl(
                new StubAuthenticationManager(),
                userMapper,
                refreshTokenMapper,
                new StubTokenManager(signedUpUser.getUserId()));
    }

    @Test
    void signin_로그인하면_토큰이_발급되어야한다() {
        SigninRequestDto presentedIdPassword = SigninRequestDto.builder()
                .userId(signedUpUser.getUserId())
                .password(signedUpUser.getPassword())
                .build();


        TokenResponseDto providedToken = sut.signin(presentedIdPassword);


        assertNotNull(providedToken);
        assertThat(providedToken.getAccessToken()).isNotBlank();
        assertThat(providedToken.getRefreshToken()).isNotBlank();
    }

    @Test
    void signin_두_번_로그인하면_토큰이_재발급되어야_한다() {
        SigninRequestDto presentedIdPassword = SigninRequestDto.builder()
                .userId(signedUpUser.getUserId())
                .password(signedUpUser.getPassword())
                .build();


        TokenResponseDto providedToken = sut.signin(presentedIdPassword);
        TokenResponseDto newToken = sut.signin(presentedIdPassword);


        assertNotEquals(providedToken, newToken);
    }

    @Test
    void signout_로그아웃하면_리프레시토큰이_삭제되어야한다() {
        signin();
        SignoutRequestDto signoutRequestDto = SignoutRequestDto.builder()
                .userId(signedUpUser.getUserId())
                .build();


        sut.signout(signoutRequestDto);


        Optional<RefreshTokenDto> foundToken = refreshTokenMapper.findTokenByKey(signedUpUser.getUserId());
        assertTrue(foundToken.isEmpty());
    }

    @Test
    void refresh_토큰을_재발급_받을수있다() {
        TokenResponseDto tokens = signin();
        TokenRequestDto refreshRequest = new TokenRequestDto(tokens.getAccessToken(), tokens.getRefreshToken());


        TokenResponseDto reissuedToken = sut.refresh(refreshRequest);


        assertNotNull(reissuedToken);
        assertThat(reissuedToken.getAccessToken()).isNotBlank();
        assertThat(reissuedToken.getRefreshToken()).isNotBlank();
    }

    private TokenResponseDto signin() {
        SigninRequestDto signinRequestDto = SigninRequestDto.builder()
                .userId(signedUpUser.getUserId())
                .password(signedUpUser.getPassword())
                .build();
        return sut.signin(signinRequestDto);
    }
}