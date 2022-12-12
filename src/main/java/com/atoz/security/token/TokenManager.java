package com.atoz.security.token;

import com.atoz.user.Authority;
import org.springframework.security.core.GrantedAuthority;

import java.util.*;

public interface TokenManager {
    String createAccessToken(String userId, Set<Authority> authorities);

    String createRefreshToken(String userId, Set<Authority> authorities);

    void validateToken(String token);

    String parseUserId(String token);

    Collection<? extends GrantedAuthority> parseGrantedAuthorities(String token);
}
