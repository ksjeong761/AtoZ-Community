package com.atoz.security.authentication.help;

import com.atoz.security.authorization.AuthorizationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;

public class StubAuthorizationProvider implements AuthorizationProvider {
    @Override
    public Authentication authorize(String jwt) {
        String principal = jwt;
        String credential = "";
        List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_FAKE"));

        return new UsernamePasswordAuthenticationToken(principal, credential, authorities);
    }

    @Override
    public Authentication authorize(String accessToken, String refreshToken) {
        String principal = accessToken;
        String credential = "";
        List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_FAKE"));

        return new UsernamePasswordAuthenticationToken(principal, credential, authorities);
    }
}
