package com.atoz.security.authorization;

import com.atoz.security.token.RefreshTokenMapper;
import com.atoz.error.exception.InvalidTokenException;
import com.atoz.security.token.TokenParser;
import com.atoz.security.token.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RequiredArgsConstructor
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String BEARER_PREFIX = "Bearer";

    private final TokenParser tokenParser;
    private final RefreshTokenMapper refreshTokenMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        if (request.getServletPath().startsWith("/auth") || request.getServletPath().startsWith("/user")) {
            filterChain.doFilter(request, response);
        } else {
            String token = resolveToken(request);

            if (StringUtils.hasText(token)) {
                String userId = tokenParser.parseUserId(token);
                refreshTokenMapper.findTokenByKey(userId).orElseThrow(() -> new InvalidTokenException("로그아웃된 사용자입니다."));

                this.setAuthentication(token);
            } else {
                throw new InvalidTokenException("토큰 값이 비어있습니다.");
            }
        }
    }

    private void setAuthentication(String token) {
        Authentication authentication = tokenParser.toAuthentication(token);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
            return bearerToken.substring(7);
        }
        return null;
    }
}