package com.atoz.security.authorization;

import com.atoz.security.token.*;
import com.atoz.security.token.helper.MockRefreshTokenMapper;
import com.atoz.user.entity.Authority;
import com.atoz.user.entity.UserEntity;
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

    private final String secretKey = "b3VyLXByb2plY3QtbmFtZS1BdG9aLWxpa2UtYmxpbmQtZm9yLWdlbmVyYXRpb24tb3VyLXByb2plY3QtbGlrZS1ibGluZC1nZW5lcmF0aW9u";
    private final TokenParser tokenParser = new TokenParser(secretKey);
    private final TokenProvider tokenProvider = new TokenProviderImpl(secretKey, 1800000L, 604800000L);

    private UserEntity signedUpUser;

    private AuthorizationProvider sut;
    private RefreshTokenMapper refreshTokenMapper = new MockRefreshTokenMapper();

    @BeforeEach
    void setUp() {
        signedUpUser = UserEntity.builder()
                .userId("testUserId")
                .password("testPassword")
                .email("test@test.com")
                .nickname("testNickname")
                .authorities(Set.of(Authority.ROLE_USER))
                .build();
        UserDetailsService userDetailsService = new MockUserDetailsService(List.of(signedUpUser));

        sut = new JwtAuthorizationProvider(
                tokenParser,
                refreshTokenMapper,
                userDetailsService);
    }

    @Test
    void authorize_토큰이_유효하면_사용자_정보가_담긴_객체가_반환된다() {
        String refreshToken = tokenProvider.createRefreshToken(signedUpUser.getUserId(), signedUpUser.getAuthorities());
        refreshTokenMapper.saveToken(new RefreshTokenEntity(signedUpUser.getUserId(), refreshToken));


        Authentication authentication = sut.authorize(refreshToken);


        UserDetails userDetails = (UserDetails)authentication.getPrincipal();
        assertNotNull(authentication);
        assertEquals(userDetails.getUsername(), signedUpUser.getUserId());
    }
}