package com.atoz.user;

import com.atoz.security.token.RefreshTokenMapper;
import com.atoz.security.token.helper.MockRefreshTokenMapper;
import com.atoz.user.dto.UserResponseDTO;
import com.atoz.user.entity.Authority;
import com.atoz.user.entity.UserEntity;
import com.atoz.user.helper.SpyUserMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Set;

import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.jupiter.api.Assertions.*;

@Slf4j
class UserServiceImplTest {

    private final SpyUserMapper userMapper = new SpyUserMapper();
    private final RefreshTokenMapper refreshTokenMapper = new MockRefreshTokenMapper();

    private final UserServiceImpl sut = new UserServiceImpl(userMapper, refreshTokenMapper);

    private UserEntity signedUpUser;

    @BeforeEach
    void setUp() {
        signedUpUser = UserEntity.builder()
                .userId("testUserId")
                .password("testPassword")
                .email("test@test.com")
                .nickname("testNickname")
                .authorities(Set.of(Authority.ROLE_USER))
                .build();
        userMapper.addUser(signedUpUser);
    }

    @Test
    void signup_회원가입하면_가입된_회원정보가_반환된다() {
        UserEntity userEntity = UserEntity.builder()
                .userId("testUserId")
                .password("testPassword")
                .email("test@test.com")
                .nickname("testNickname")
                .authorities(Set.of(Authority.ROLE_USER))
                .build();


        UserResponseDTO response = sut.signup(userEntity);


        assertNotNull(response);
        assertEquals(response.getUserId(), userEntity.getUserId());
    }

    @Test
    void loadUserByUsername_가입된_회원정보를_조회할수있다() {
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