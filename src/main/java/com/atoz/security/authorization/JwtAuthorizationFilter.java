package com.atoz.security.authorization;

import com.atoz.error.exception.InvalidTokenException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RequiredArgsConstructor
@Component
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String BEARER_PREFIX = "Bearer";

    private final AuthorizationProvider authorizationProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        if (request.getServletPath().startsWith("/auth") || request.getServletPath().startsWith("/user")) {
            filterChain.doFilter(request, response);
            return;
        }

        // 요청받은 토큰이 유효한지 확인한다.
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        String jwt = resolveBearerToken(bearerToken);
        Authentication authentication = authorizationProvider.authorize(jwt);

        // 유효하다면 사용자 정보를 저장해둔다.
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private String resolveBearerToken(String bearerToken) {
        if (!StringUtils.hasText(bearerToken)) {
            throw new InvalidTokenException("bearer 토큰이 비어있습니다.");
        }

        if (!bearerToken.startsWith(BEARER_PREFIX)) {
            throw new InvalidTokenException("bearer 토큰 형식이 잘못되었습니다.");
        }

        return bearerToken.substring(7);
    }
}