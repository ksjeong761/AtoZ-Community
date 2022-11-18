package com.atoz.authentication;

import com.atoz.authentication.testDouble.TestAuthMapper;
import com.atoz.authentication.testDouble.TestDoubleAuthService;
import com.atoz.error.InvalidTokenException;
import com.atoz.user.SigninDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class AuthServiceTest {

    private TestDoubleAuthService authService;
    private TestAuthMapper authMapper = new TestAuthMapper();

    @BeforeEach
    public void beforeEach() {
        authService = new TestDoubleAuthService();
    }

    @Test
    void signin_로그인에_성공한다() {
        SigninDTO presentedIdPassword = SigninDTO.builder()
                .userId("testId")
                .password("testPassword")
                .build();

        TokenDTO signin = authService.signin(presentedIdPassword);

        RefreshToken savedRefreshToken = authMapper.findByKey(presentedIdPassword.getUserId()).orElse(null);
        assertThat(signin.getGrantType()).isEqualTo("Bearer");
        assertThat(savedRefreshToken).isNotNull();
    }

    @Test
    void signin_아이디가_없는경우_로그인에_실패한다() {
        SigninDTO presentedIdPassword = SigninDTO.builder()
                .userId("testId2")
                .password("testPassword")
                .build();

        assertThatThrownBy(() -> authService.signin(presentedIdPassword))
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessage("해당 유저가 존재하지 않습니다.");
    }

    @Test
    void signin_비밀번호가_틀린경우_로그인에_실패한다() {
        SigninDTO presentedIdPassword = SigninDTO.builder()
                .userId("testId")
                .password("testPassword2")
                .build();

        assertThatThrownBy(() -> authService.signin(presentedIdPassword))
                .isInstanceOf(BadCredentialsException.class)
                .hasMessage("패스워드가 틀립니다.");
    }

    @Test
    void signout_로그아웃에_성공한다() {
        SigninDTO presentedIdPassword = SigninDTO.builder()
                .userId("testId")
                .password("testPassword")
                .build();
        TokenDTO signin = authService.signin(presentedIdPassword);
        TokenRequestDTO tokenRequestDTO = new TokenRequestDTO(signin.getAccessToken(), signin.getRefreshToken());

        authService.signout(tokenRequestDTO);

        RefreshToken deletedToken = authMapper.findByKey(presentedIdPassword.getUserId()).orElse(null);
        assertThat(deletedToken).isNull();
    }

    @Test
    void signout_인증된상태가_아닌경우_로그아웃에_실패한다() {
        SigninDTO presentedIdPassword = SigninDTO.builder()
                .userId("testId")
                .password("testPassword")
                .build();
        TokenDTO signin = authService.signin(presentedIdPassword);
        TokenRequestDTO tokenRequestDTO = new TokenRequestDTO(signin.getAccessToken(), signin.getRefreshToken());
        authService.signout(tokenRequestDTO);

        assertThatThrownBy(() -> authService.signout(tokenRequestDTO))
                .isInstanceOf(InvalidTokenException.class)
                .hasMessage("인증정보가 없습니다.");
    }

    @Test
    void reissue_토큰재발급에_성공한다() throws InterruptedException {
        //given
        SigninDTO presentedIdPassword = SigninDTO.builder()
                .userId("testId")
                .password("testPassword")
                .build();
        TokenDTO orgToken = authService.signin(presentedIdPassword);
        TokenRequestDTO tokenRequestDTO = new TokenRequestDTO(orgToken.getAccessToken(), orgToken.getRefreshToken());
        Thread.sleep(1000L);

        //when
        TokenDTO reissuedToken = authService.refresh(tokenRequestDTO);

        //then
        assertThat(reissuedToken.getGrantType()).isEqualTo("Bearer");
        assertThat(reissuedToken.getAccessToken()).isNotEqualTo(orgToken.getAccessToken());
        assertThat(reissuedToken.getRefreshToken()).isNotEqualTo(orgToken.getRefreshToken());
    }
}