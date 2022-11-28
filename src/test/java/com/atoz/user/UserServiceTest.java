package com.atoz.user;

import com.atoz.user.dto.UserResponseDTO;
import com.atoz.user.entity.Authority;
import com.atoz.user.entity.UserEntity;
import com.atoz.user.help.SpyUserMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
class UserServiceTest {

    private final UserService sut = new UserServiceImpl(new SpyUserMapper());

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


        assertThat(response).isNotNull();
        assertThat(response.getUserId()).isEqualTo(userEntity.getUserId());
    }
}