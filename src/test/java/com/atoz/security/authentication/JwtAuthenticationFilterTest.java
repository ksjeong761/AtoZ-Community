package com.atoz.security.authentication;

import com.atoz.security.authentication.helper.SpyStubAuthenticationEntryPoint;
import com.atoz.security.token.RefreshTokenMapper;
import com.atoz.security.token.TokenManager;
import com.atoz.security.token.dto.RefreshTokenDto;
import com.atoz.security.token.helper.MockRefreshTokenMapper;
import com.atoz.security.token.helper.StubTokenManager;
import com.atoz.user.Authority;
import com.atoz.user.dto.UserDto;
import com.atoz.user.helper.MockUserDetailsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import javax.servlet.ServletException;
import java.io.IOException;
import java.util.List;
import java.util.Set;

import static com.atoz.security.SecurityConstants.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class JwtAuthenticationFilterTest {

    private JwtAuthenticationFilter sut;
    private TokenManager tokenManager;
    private RefreshTokenMapper refreshTokenMapper;
    private UserDetailsService userDetailsService;
    private SpyStubAuthenticationEntryPoint authenticationEntryPoint;

    private MockHttpServletRequest request;
    private MockHttpServletResponse response;
    private MockFilterChain filterChain;

    private UserDto userDto;

    @BeforeEach
    void setUp() {
        userDto = UserDto.builder()
                .userId("testUserId")
                .password("testPassword")
                .nickname("testNickname")
                .email("test@test.com")
                .authorities(Set.of(Authority.ROLE_USER))
                .build();

        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
        filterChain = new MockFilterChain();

        tokenManager = new StubTokenManager(userDto.getUserId());
        refreshTokenMapper = new MockRefreshTokenMapper();
        userDetailsService = new MockUserDetailsService(List.of(userDto));
        authenticationEntryPoint = new SpyStubAuthenticationEntryPoint();

        sut = new JwtAuthenticationFilter(tokenManager, refreshTokenMapper, userDetailsService, authenticationEntryPoint);
    }

    @Test
    void doFilterInternal_인증에_성공하면_DB에서_읽어온_사용자_정보가_SecurityContextHolder에_저장된다() throws ServletException, IOException {
        saveRefreshToken(userDto.getUserId());
        String accessToken = tokenManager.createAccessToken(userDto.getUserId(), userDto.getAuthorities());
        request.addHeader(AUTHORIZATION_HEADER, BEARER_PREFIX + accessToken);


        sut.doFilterInternal(request, response, filterChain);


        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails principal = (UserDetails)authentication.getPrincipal();
        assertNotNull(authentication);
        assertEquals(userDto.getUserId(), principal.getUsername());
        assertEquals(userDto.toUserDetails().getAuthorities(), principal.getAuthorities());
    }

    @Test
    void doFilterInternal_인증에_성공하면_다음_필터로_작업을_넘긴다() throws ServletException, IOException {
        saveRefreshToken(userDto.getUserId());
        String accessToken = tokenManager.createAccessToken(userDto.getUserId(), userDto.getAuthorities());
        request.addHeader(AUTHORIZATION_HEADER, BEARER_PREFIX + accessToken);

        MockFilterChain spyMockFilterChain = spy(filterChain);


        sut.doFilterInternal(request, response, spyMockFilterChain);


        verify(spyMockFilterChain, times(1)).doFilter(request, response);
    }

    @Test
    void doFilterInternal_JWT가_요청_헤더에_없어도_다음_필터로_작업을_넘긴다() throws ServletException, IOException {
        saveRefreshToken(userDto.getUserId());
        MockFilterChain spyMockFilterChain = spy(filterChain);


        sut.doFilterInternal(request, response, spyMockFilterChain);


        verify(spyMockFilterChain, times(1)).doFilter(request, response);
    }

    @Test
    void doFilterInternal_Bearer_토큰_형식이_잘못되어도_다음_필터로_작업을_넘긴다() throws ServletException, IOException {
        saveRefreshToken(userDto.getUserId());
        String accessToken = tokenManager.createAccessToken(userDto.getUserId(), userDto.getAuthorities());
        request.addHeader(AUTHORIZATION_HEADER, "Wrong bearer token" + accessToken);

        MockFilterChain spyMockFilterChain = spy(filterChain);


        sut.doFilterInternal(request, response, spyMockFilterChain);


        verify(spyMockFilterChain, times(1)).doFilter(request, response);
    }

    @Test
    void doFilterInternal_리프레시_토큰이_저장되어_있지_않으면_EntryPoint에_작업을_넘긴다() throws ServletException, IOException {
        String accessToken = tokenManager.createAccessToken(userDto.getUserId(), userDto.getAuthorities());
        request.addHeader(AUTHORIZATION_HEADER, BEARER_PREFIX + accessToken);


        sut.doFilterInternal(request, response, filterChain);


        assertEquals(1, authenticationEntryPoint.callCountCommence);
    }

    private void saveRefreshToken(String userId) {
        RefreshTokenDto refreshTokenDto = RefreshTokenDto.builder()
                .tokenKey(userId)
                .tokenValue("testRefreshToken")
                .build();
        refreshTokenMapper.saveToken(refreshTokenDto);
    }
}
