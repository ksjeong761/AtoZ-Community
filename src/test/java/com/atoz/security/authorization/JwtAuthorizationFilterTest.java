package com.atoz.security.authorization;

import com.atoz.security.authorization.helper.StubAuthorizationProvider;
import com.atoz.security.token.TokenProvider;
import com.atoz.security.token.TokenProviderImpl;
import com.atoz.user.entity.Authority;
import com.atoz.user.entity.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.servlet.ServletException;
import java.io.IOException;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

public class JwtAuthorizationFilterTest {

    private final String AUTHORIZATION_HEADER = "Authorization";
    private final String BEARER_PREFIX = "Bearer ";

    private final JwtAuthorizationFilter sut = new JwtAuthorizationFilter(new StubAuthorizationProvider());
    private MockHttpServletRequest request;
    private MockHttpServletResponse response;
    private MockFilterChain filterChain;
    private UserEntity userEntity;
    private TokenProvider tokenProvider;

    @BeforeEach
    void setUp() {
        userEntity = UserEntity.builder()
                .userId("testUserId")
                .password("testPassword")
                .nickname("testNickname")
                .email("test@test.com")
                .authorities(Set.of(Authority.ROLE_USER))
                .build();
        tokenProvider = new TokenProviderImpl(
                "b3VyLXByb2plY3QtbmFtZS1BdG9aLWxpa2UtYmxpbmQtZm9yLWdlbmVyYXRpb24tb3VyLXByb2plY3QtbGlrZS1ibGluZC1nZW5lcmF0aW9u",
                1800000L,
                604800000L);

        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
        filterChain = new MockFilterChain();
    }

    @Test
    void doFilterInternal_인가된_사용자_정보가_SecurityContextHolder에_저장된다() throws ServletException, IOException {
        String jwt = tokenProvider.createAccessToken(userEntity.getUserId(), userEntity.getAuthorities());
        request.addHeader(AUTHORIZATION_HEADER, BEARER_PREFIX + jwt);


        sut.doFilterInternal(request, response, filterChain);


        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        assertNotNull(authentication);
        assertEquals(authentication.getPrincipal().toString(), jwt);
    }

    @Test
    void doFilterInternal_jwt를_디코딩하고_다음_필터로_작업을_넘긴다() throws ServletException, IOException {
        MockFilterChain spyMockFilterChain = spy(filterChain);


        sut.doFilter(request, response, spyMockFilterChain);


        verify(spyMockFilterChain, times(1)).doFilter(request, response);
    }

    @Test
    void doFilterInternal_jwt가_없는_경우에도_다음_필터로_작업을_넘긴다() throws ServletException, IOException {
        MockFilterChain spyMockFilterChain = spy(filterChain);
        MockHttpServletRequest requestWithoutHeader = new MockHttpServletRequest();


        sut.doFilter(requestWithoutHeader, response, spyMockFilterChain);


        verify(spyMockFilterChain, times(1)).doFilter(requestWithoutHeader, response);
    }
}
