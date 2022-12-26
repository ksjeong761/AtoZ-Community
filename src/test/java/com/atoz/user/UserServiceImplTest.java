package com.atoz.user;

import com.atoz.security.authentication.helper.CustomWithMockUser;
import com.atoz.security.token.dto.RefreshTokenDto;
import com.atoz.security.token.RefreshTokenMapper;
import com.atoz.security.token.helper.MockRefreshTokenMapper;
import com.atoz.user.dto.request.ChangePasswordRequestDto;
import com.atoz.user.dto.response.UserResponseDto;
import com.atoz.user.dto.UserDto;
import com.atoz.user.helper.SpyUserMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration
class UserServiceImplTest {

    private final SpyUserMapper userMapper = new SpyUserMapper();
    private final RefreshTokenMapper refreshTokenMapper = new MockRefreshTokenMapper();

    private final UserServiceImpl sut = new UserServiceImpl(userMapper, refreshTokenMapper);

    private final String TEST_USER_ID = "testUserId";

    @BeforeEach
    void setUp() {
        UserDto signedUpUser = UserDto.builder()
                .userId(TEST_USER_ID)
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
        assertEquals(userDto.getUserId(), response.getUserId());
    }

    @Test
    @CustomWithMockUser(username = TEST_USER_ID)
    void changePassword_비밀번호를_변경하면_리프레시_토큰이_삭제된다() {
        saveToken();

        ChangePasswordRequestDto changePasswordRequestDto = ChangePasswordRequestDto.builder()
                .password("newPassword")
                .build();


        sut.changePassword(changePasswordRequestDto);


        Optional<RefreshTokenDto> foundToken = refreshTokenMapper.findTokenByKey(TEST_USER_ID);
        assertTrue(foundToken.isEmpty());
    }

    @Test
    @CustomWithMockUser(username = TEST_USER_ID)
    void delete_회원탈퇴하면_리프레시_토큰이_삭제된다() {
        saveToken();


        sut.delete();


        Optional<RefreshTokenDto> foundToken = refreshTokenMapper.findTokenByKey(TEST_USER_ID);
        assertTrue(foundToken.isEmpty());
    }

    @Test
    void loadUserByUsername_가입된_회원정보를_조회할_수_있다() {
        String username = TEST_USER_ID;


        UserDetails userDetails = sut.loadUserByUsername(username);


        assertNotNull(userDetails);
        assertEquals(username, userDetails.getUsername());
    }

    @Test
    void loadUserByUsername_가입되지않은_회원을_조회하면_예외가_발생한다() {
        String wrongUserId = "wrongUserId";


        Throwable thrown = catchThrowable(() -> {
            sut.loadUserByUsername(wrongUserId);
        });


        assertInstanceOf(UsernameNotFoundException.class, thrown);
    }

    private void saveToken() {
        RefreshTokenDto refreshToken = RefreshTokenDto.builder()
                .tokenKey(TEST_USER_ID)
                .tokenValue("refreshToken")
                .build();
        refreshTokenMapper.saveToken(refreshToken);
    }
}