package com.atoz.user;

import com.atoz.error.SigninFailedException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

@Slf4j
class UserServiceTest {

    SpyStubUserMapper userMapper = new SpyStubUserMapper();

    UserService userService = new UserServiceImpl(userMapper);

    @Test
    void signin_로그인에_성공해야한다() {
        SigninDTO signinDTO = SigninDTO.builder()
                .userId("testUserId")
                .password("testPassword")
                .build();

        UserResponseDTO user = userService.signin(signinDTO);

        assertThat(userMapper.getCallFindByIdCount()).isEqualTo(1);
        assertThat(signinDTO.getUserId()).isEqualTo(user.getUserId());
    }

    @Test
    void signin_아이디가_가입되어있지_않으면_로그인에_실패해야한다() {
        SigninDTO expectedSigninDTO = SigninDTO.builder()
                .userId("testUserId2")
                .password("testPassword")
                .build();

        assertThatThrownBy(() -> userService.signin(expectedSigninDTO))
                .isInstanceOf(SigninFailedException.class);
        assertThat(userMapper.getCallFindByIdCount()).isEqualTo(1);
    }

    @Test
    void signin_비밀번호가_틀리면_로그인에_실패해야한다() {
        SigninDTO expectedSigninDTO = SigninDTO.builder()
                .userId("testId")
                .password("testPassword2")
                .build();

        assertThatThrownBy(() ->userService.signin(expectedSigninDTO))
                .isInstanceOf(SigninFailedException.class);
        assertThat(userMapper.getCallFindByIdCount()).isEqualTo(1);
    }

    private static class SpyStubUserMapper implements UserMapper {

        private int callFindByIdCount = 0;

        @Override
        public void addUser(UserEntity signupEntity) { }

        @Override
        public UserEntity findById(String userId) {
            this.callFindByIdCount++;

            if (!userId.equals("testUserId")) {
                return null;
            }

            SignupDTO signupDTO = new SignupDTO("testUserId", "testPassword", "testNickname", "test@test.com");
            return new UserEntity(signupDTO);
        }

        public int getCallFindByIdCount() {
            return callFindByIdCount;
        }
    }
}