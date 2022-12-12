package com.atoz.security.authorization;

import com.atoz.security.token.*;
import com.atoz.security.token.dto.RefreshTokenDto;
import com.atoz.security.token.helper.MockRefreshTokenMapper;
import com.atoz.user.Authority;
import com.atoz.user.dto.UserDto;
import com.atoz.user.helper.MockUserDetailsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class JwtAuthorizationProviderTest {

    private final TokenManager tokenManager = new TokenManagerImpl();

    private UserDto signedUpUser;

    private AuthorizationProvider sut;
    private RefreshTokenMapper refreshTokenMapper = new MockRefreshTokenMapper();

    @BeforeEach
    void setUp() {
        signedUpUser = UserDto.builder()
                .userId("testUserId")
                .password("testPassword")
                .email("test@test.com")
                .nickname("testNickname")
                .authorities(Set.of(Authority.ROLE_USER))
                .build();
        UserDetailsService userDetailsService = new MockUserDetailsService(List.of(signedUpUser));

        sut = new JwtAuthorizationProvider(
                tokenManager,
                refreshTokenMapper,
                userDetailsService);
    }

    @Test
    void authorize_토큰이_유효하면_사용자_정보가_담긴_객체가_반환된다() {
        String refreshToken = tokenManager.createRefreshToken(signedUpUser.getUserId(), signedUpUser.getAuthorities());
        refreshTokenMapper.saveToken(new RefreshTokenDto(signedUpUser.getUserId(), refreshToken));


        Authentication authentication = sut.authorize(refreshToken);


        UserDetails userDetails = (UserDetails)authentication.getPrincipal();
        assertNotNull(authentication);
        assertEquals(userDetails.getUsername(), signedUpUser.getUserId());
    }
}