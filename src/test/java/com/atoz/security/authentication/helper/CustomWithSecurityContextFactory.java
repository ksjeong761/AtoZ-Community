package com.atoz.security.authentication.helper;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import java.util.List;

public class CustomWithSecurityContextFactory implements WithSecurityContextFactory<CustomWithMockUser> {
    @Override
    public SecurityContext createSecurityContext(CustomWithMockUser annotation) {
        UserDetails principal = User.builder()
                .username(annotation.username())
                .password(annotation.password())
                .authorities(List.of(new SimpleGrantedAuthority(annotation.role())))
                .build();

        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Authentication authentication = new UsernamePasswordAuthenticationToken(principal, principal.getPassword(), principal.getAuthorities());
        context.setAuthentication(authentication);
        return context;
    }
}
