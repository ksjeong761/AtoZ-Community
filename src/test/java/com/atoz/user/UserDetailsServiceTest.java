package com.atoz.user;

import com.atoz.user.entity.Authority;
import com.atoz.user.entity.UserEntity;
import com.atoz.user.help.SpyUserMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

public class UserDetailsServiceTest {

    private final UserMapper userMapper = new SpyUserMapper();
    private final UserDetailsService sut = new UserServiceImpl(userMapper);

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
    void loadUserByUsername_가입된_회원정보를_조회할수있다() {
        String username = signedUpUser.getUserId();


        UserDetails userDetails = sut.loadUserByUsername(username);


        assertThat(userDetails).isNotNull();
        assertThat(userDetails.getUsername()).isEqualTo(signedUpUser.getUserId());
    }

    @Test
    void loadUserByUsername_가입되지않은_회원을_조회하면_예외가_발생한다() {
        String wrongUserId = "wrongUserId";


        Throwable thrown = catchThrowable(() -> {
            sut.loadUserByUsername(wrongUserId);
        });


        assertThat(thrown).isInstanceOf(UsernameNotFoundException.class);
    }
}
