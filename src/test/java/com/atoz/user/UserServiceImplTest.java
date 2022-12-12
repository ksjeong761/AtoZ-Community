package com.atoz.user;

import com.atoz.security.token.dto.RefreshTokenDto;
import com.atoz.security.token.RefreshTokenMapper;
import com.atoz.security.token.helper.MockRefreshTokenMapper;
import com.atoz.user.dto.request.ChangePasswordRequestDto;
import com.atoz.user.dto.response.UserResponseDto;
import com.atoz.user.dto.UserDto;
import com.atoz.user.helper.SpyUserMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.jupiter.api.Assertions.*;

@Slf4j
class UserServiceImplTest {

    private final SpyUserMapper userMapper = new SpyUserMapper();
    private final RefreshTokenMapper refreshTokenMapper = new MockRefreshTokenMapper();

    private final UserServiceImpl sut = new UserServiceImpl(userMapper, refreshTokenMapper);

    private UserDto signedUpUser;

    @BeforeEach
    void setUp() {
        signedUpUser = UserDto.builder()
                .userId("testUserId")
                .password("testPassword")
                .email("test@test.com")
                .nickname("testNickname")
                .authorities(Set.of(Authority.ROLE_USER))
                .build();
        userMapper.addUser(signedUpUser);
    }

    @Test
    void signup_회원가입하면_가입된_회원_정보가_반환된다() {
        UserDto userDto = UserDto.builder()
                .userId("testUserId")
                .password("testPassword")
                .email("test@test.com")
                .nickname("testNickname")
                .authorities(Set.of(Authority.ROLE_USER))
                .build();


        UserResponseDto response = sut.signup(userDto);


        assertNotNull(response);
        assertEquals(response.getUserId(), userDto.getUserId());
    }

    @Test
    void changePassword_비밀번호를_변경하면_리프레시_토큰이_만료된다() {
        ChangePasswordRequestDto changePasswordRequestDto = ChangePasswordRequestDto.builder()
                .userId(signedUpUser.getUserId())
                .password("newPassword")
                .build();
        RefreshTokenDto refreshToken = RefreshTokenDto.builder()
                .tokenKey(signedUpUser.getUserId())
                .tokenValue("refreshToken")
                .build();
        refreshTokenMapper.saveToken(refreshToken);


        sut.changePassword(changePasswordRequestDto);


        Optional<RefreshTokenDto> foundToken = refreshTokenMapper.findTokenByKey(signedUpUser.getUserId());
        assertTrue(foundToken.isEmpty());
    }

    @Test
    void delete_회원탈퇴하면_리프레시_토큰이_만료된다() {
        RefreshTokenDto refreshToken = RefreshTokenDto.builder()
                .tokenKey(signedUpUser.getUserId())
                .tokenValue("refreshToken")
                .build();
        refreshTokenMapper.saveToken(refreshToken);


        sut.delete(signedUpUser.getUserId());


        Optional<RefreshTokenDto> foundToken = refreshTokenMapper.findTokenByKey(signedUpUser.getUserId());
        assertTrue(foundToken.isEmpty());
    }

    @Test
    void loadUserByUsername_가입된_회원정보를_조회할_수_있다() {
        String username = signedUpUser.getUserId();


        UserDetails userDetails = sut.loadUserByUsername(username);


        assertNotNull(userDetails);
        assertEquals(userDetails.getUsername(), signedUpUser.getUserId());
    }

    @Test
    void loadUserByUsername_가입되지않은_회원을_조회하면_예외가_발생한다() {
        String wrongUserId = "wrongUserId";


        Throwable thrown = catchThrowable(() -> {
            sut.loadUserByUsername(wrongUserId);
        });


        assertInstanceOf(UsernameNotFoundException.class, thrown);
    }
}