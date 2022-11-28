package com.atoz.security.token;

import com.atoz.user.entity.Authority;

import java.util.*;

public interface TokenProvider {
    String createAccessToken(String userId, Set<Authority> authorities);

    String createRefreshToken(String userId, Set<Authority> authorities);
}
