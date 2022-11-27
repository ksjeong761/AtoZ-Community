package com.atoz.security.authorization;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RequiredArgsConstructor
@Component
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final JwtAuthorizationProvider jwtAuthorizationProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        if (!request.getServletPath().startsWith("/auth") && !request.getServletPath().startsWith("/user")) {
            filterChain.doFilter(request, response);
            return;
        }

        // 요청받은 토큰이 유효한지 확인한다.
        Authentication authentication = jwtAuthorizationProvider.authorizeBearerToken(request);

        // 유효하다면 사용자 정보를 저장해둔다.
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}