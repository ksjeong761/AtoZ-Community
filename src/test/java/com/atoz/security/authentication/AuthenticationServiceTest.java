package com.atoz.security.authentication;

import com.atoz.security.authentication.dto.SignoutDTO;
import com.atoz.security.authentication.helper.StubAuthenticationManager;
import com.atoz.security.authorization.helper.StubAuthorizationProvider;
import com.atoz.security.token.helper.MockRefreshTokenMapper;
import com.atoz.security.token.helper.StubTokenManager;
import com.atoz.security.authentication.dto.TokenRequestDTO;
import com.atoz.security.token.RefreshTokenEntity;
import com.atoz.security.authentication.dto.TokenResponseDTO;
import com.atoz.security.authentication.dto.SigninDTO;
import com.atoz.security.token.RefreshTokenMapper;
import com.atoz.user.UserMapper;
import com.atoz.user.entity.Authority;
import com.atoz.user.entity.UserEntity;
import com.atoz.user.helper.SpyUserMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AuthenticationServiceTest {

    private final UserMapper userMapper = new SpyUserMapper();
    private final RefreshTokenMapper refreshTokenMapper = new MockRefreshTokenMapper();
    private final AuthenticationService sut = new AuthenticationServiceImpl(
            new StubAuthenticationManager(),
            userMapper,
            refreshTokenMapper,
            new StubTokenManager());

    private UserEntity signedUpUser;

    @BeforeEach
    private void setUp() {
        signedUpUser = UserEntity.builder()
                .userId("testUserId")
                .password("testPassword")
                .nickname("testNickname")
                .email("test@test.com")
                .authorities(Set.of(Authority.ROLE_USER))
                .build();
        userMapper.addUser(signedUpUser);
    }

    @Test
    void signin_로그인하면_토큰이_발급되어야한다() {
        SigninDTO presentedIdPassword = SigninDTO.builder()
                .userId(signedUpUser.getUserId())
                .password(signedUpUser.getPassword())
                .build();


        TokenResponseDTO providedToken = sut.signin(presentedIdPassword);


        assertNotNull(providedToken);
        assertThat(providedToken.getAccessToken()).isNotBlank();
        assertThat(providedToken.getRefreshToken()).isNotBlank();
    }

    @Test
    void signout_로그아웃하면_리프레시토큰이_삭제되어야한다() {
        SigninDTO signinDTO = SigninDTO.builder()
                .userId(signedUpUser.getUserId())
                .password(signedUpUser.getPassword())
                .build();
        sut.signin(signinDTO);

        SignoutDTO signoutDTO = SignoutDTO.builder()
                .userId(signedUpUser.getUserId())
                .build();


        sut.signout(signoutDTO);


        Optional<RefreshTokenEntity> foundToken = refreshTokenMapper.findTokenByKey(signedUpUser.getUserId());
        assertTrue(foundToken.isEmpty());
    }

    @Test
    void refresh_토큰을_재발급_받을수있다() {
        SigninDTO signinDTO = SigninDTO.builder()
                .userId(signedUpUser.getUserId())
                .password(signedUpUser.getPassword())
                .build();
        TokenResponseDTO tokens = sut.signin(signinDTO);
        TokenRequestDTO refreshRequest = new TokenRequestDTO(tokens.getAccessToken(), tokens.getRefreshToken());


        TokenResponseDTO reissuedToken = sut.refresh(refreshRequest);


        assertNotNull(reissuedToken);
        assertThat(reissuedToken.getAccessToken()).isNotBlank();
        assertThat(reissuedToken.getRefreshToken()).isNotBlank();
    }
}