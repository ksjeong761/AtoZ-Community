package com.atoz.security.authentication;

import com.atoz.error.exception.JwtAuthenticationException;
import com.atoz.security.token.RefreshTokenMapper;
import com.atoz.security.token.TokenManager;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.atoz.security.SecurityConstants.AUTHORIZATION_HEADER;
import static com.atoz.security.SecurityConstants.BEARER_PREFIX;

@RequiredArgsConstructor
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final TokenManager tokenManager;
    private final RefreshTokenMapper refreshTokenMapper;
    private final UserDetailsService userDetailsService;
    private final AuthenticationEntryPoint authenticationEntryPoint;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        if (bearerToken != null && bearerToken.startsWith(BEARER_PREFIX)) {
            try {
                String accessToken = bearerToken.substring(7);

                Authentication authentication = authenticate(accessToken);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } catch (AuthenticationException ex) {
                authenticationEntryPoint.commence(request, response, ex);
            }
        }

        filterChain.doFilter(request, response);
    }

    private Authentication authenticate(String accessToken) {
        tokenManager.validateToken(accessToken);
        String userId = tokenManager.parseUserId(accessToken);

        checkRefreshTokenExists(userId);

        UserDetails principal = userDetailsService.loadUserByUsername(userId);
        return new UsernamePasswordAuthenticationToken(
                principal,
                principal.getPassword(),
                principal.getAuthorities());
    }

    private void checkRefreshTokenExists(String userId) {
        refreshTokenMapper.findTokenByKey(userId)
                .orElseThrow(() -> new JwtAuthenticationException("로그아웃된 사용자입니다."));
    }
}