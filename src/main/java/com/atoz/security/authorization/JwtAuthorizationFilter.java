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

import static com.atoz.security.SecurityConstants.AUTHORIZATION_HEADER;
import static com.atoz.security.SecurityConstants.BEARER_PREFIX;

@RequiredArgsConstructor
@Component
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final AuthorizationProvider authorizationProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        if (bearerToken != null && bearerToken.startsWith(BEARER_PREFIX)) {
            String jwt = bearerToken.substring(7);
            Authentication authentication = authorizationProvider.authorize(jwt);

            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }
}