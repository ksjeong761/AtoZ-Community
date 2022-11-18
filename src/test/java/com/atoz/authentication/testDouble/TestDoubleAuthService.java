package com.atoz.authentication.testDouble;


import com.atoz.authentication.*;
import com.atoz.error.InvalidTokenException;
import com.atoz.user.SigninDTO;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import java.util.HashSet;
import java.util.Set;

public class TestDoubleAuthService {
    private String secretKey = "b3VyLXByb2plY3QtbmFtZS1BdG9aLWxpa2UtYmxpbmQtZm9yLWdlbmVyYXRpb24tb3VyLXByb2plY3QtbGlrZS1ibGluZC1nZW5lcmF0aW9u";
    private TestDoubleCustomUserIdPasswordAuthProvider authenticationManager =
            new TestDoubleCustomUserIdPasswordAuthProvider();
    private TestDoubleCustomUserDetailService customUserDetailService;
    private TokenProvider tokenProvider = new TokenProvider(secretKey, 1800000, 604800000);
    private AuthMapper authMapper = new TestAuthMapper();

    public TokenDTO signin(SigninDTO signinDTO) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(
                        signinDTO.getUserId(), signinDTO.getPassword());

        Authentication authenticate = authenticationManager
                .authenticate(authenticationToken);
        String userId = authenticate.getName();

        customUserDetailService = new TestDoubleCustomUserDetailService();
        JwtSigninDTO user = customUserDetailService.getUser(userId);

        Set<Authority> authorities = new HashSet<>();
        authorities.add(user.getAuthority());

        String accessToken = tokenProvider.createAccessToken(signinDTO.getUserId(), authorities);
        String refreshToken = tokenProvider.createRefreshToken(userId, authorities);

        authMapper.saveRefreshToken(RefreshToken.builder()
                .tokenKey(userId)
                .tokenValue(refreshToken)
                .build());

        return tokenProvider.createTokenDTO(accessToken, refreshToken);
    }

    public void signout(TokenRequestDTO tokenRequestDTO) {
        String accessToken = tokenRequestDTO.getAccessToken();
        tokenProvider.validateToken(accessToken);
        Authentication authentication = tokenProvider.getAuthentication(accessToken);

        authMapper.findByKey(authentication.getName()).orElseThrow(() -> new InvalidTokenException("인증정보가 없습니다."));

        authMapper.deleteRefreshToken(authentication.getName());
    }

    public TokenDTO refresh(TokenRequestDTO tokenRequestDTO) {
        String orgAccessToken = tokenRequestDTO.getAccessToken();
        String orgRefreshToken = tokenRequestDTO.getRefreshToken();

        Authentication authentication = tokenProvider.getAuthentication(orgAccessToken);

        String userId = tokenProvider.getUserIdByToken(orgAccessToken);
        JwtSigninDTO user = customUserDetailService.getUser(userId);

        Set<Authority> authorities = new HashSet<>();
        authorities.add(user.getAuthority());

        String newAccessToken = tokenProvider.createAccessToken(userId, authorities);
        String newRefreshToken = tokenProvider.createRefreshToken(userId, authorities);
        TokenDTO tokenDTO = tokenProvider.createTokenDTO(newAccessToken, newRefreshToken);

        RefreshToken saveRefreshToken = RefreshToken.builder()
                .tokenKey(userId)
                .tokenValue(newRefreshToken)
                .build();

        authMapper.saveRefreshToken(saveRefreshToken);

        return tokenDTO;
    }
}