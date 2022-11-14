package com.atoz.authentication;

import com.atoz.error.InvalidTokenException;
import com.atoz.user.SigninDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final AuthMapper authMapper;
    private final TokenProvider tokenProvider;
    private final CustomUserDetailService customUserDetailService;

    @Transactional
    public TokenDTO signin(SigninDTO signinDTO) {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(signinDTO.getUserId(), signinDTO.getPassword());

        Authentication authenticate = authenticationManager.authenticate(usernamePasswordAuthenticationToken);
        String userId = authenticate.getName();

        JwtSigninDTO user = customUserDetailService.getUser(userId);

        Set<Authority> authorities = new HashSet<>();
        authorities.add(user.getAuthority());

        String accessToken = tokenProvider.createAccessToken(userId, authorities);
        String refreshToken = tokenProvider.createRefreshToken(userId, authorities);

        RefreshToken orgRefreshToken = authMapper.findByKey(user.getUserId()).orElse(null);

        // refresh token 저장
        RefreshToken newRefreshToken = RefreshToken.builder()
                .tokenKey(userId)
                .tokenValue(refreshToken)
                .build();

        if (orgRefreshToken == null) {
            authMapper.saveRefreshToken(newRefreshToken);
        } else {
            authMapper.updateRefreshToken(newRefreshToken);
        }

        return tokenProvider.createTokenDTO(accessToken, refreshToken);
    }

    @Transactional
    public void signout(TokenRequestDTO tokenRequestDTO) {
        String accessToken = tokenRequestDTO.getAccessToken();
        tokenProvider.validateToken(accessToken);

        Authentication authentication = tokenProvider.getAuthentication(accessToken);
        String tokenKey = authentication.getName();

        authMapper.findByKey(tokenKey).orElseThrow(() -> new InvalidTokenException("인증정보가 없습니다."));
        authMapper.deleteRefreshToken(authentication.getName());
    }

    @Transactional
    public TokenDTO reissue(TokenRequestDTO tokenRequestDTO) {
        String originAccessToken = tokenRequestDTO.getAccessToken();
        String originRefreshToken = tokenRequestDTO.getRefreshToken();

        tokenProvider.validateToken(originRefreshToken);

        Authentication authentication = tokenProvider.getAuthentication(originAccessToken);

        RefreshToken refreshToken = authMapper.findByKey(authentication.getName())
                .orElseThrow(() -> new InvalidTokenException("로그아웃된 사용자"));

        if (!refreshToken.getTokenValue().equals(originRefreshToken)) {
            throw new InvalidTokenException("토큰이 일치하지 않습니다.");
        }

        String userId = tokenProvider.getUserIdByToken(originAccessToken);
        JwtSigninDTO user = customUserDetailService.getUser(userId);

        Set<Authority> authorities = new HashSet<>();
        authorities.add(user.getAuthority());

        String newAccessToken = tokenProvider.createAccessToken(userId, authorities);
        String newRefreshToken = tokenProvider.createRefreshToken(userId, authorities);
        TokenDTO tokenDTO = tokenProvider.createTokenDTO(newAccessToken, newRefreshToken);

        RefreshToken reissuedToken = RefreshToken.builder()
                .tokenKey(userId)
                .tokenValue(newRefreshToken)
                .build();

        authMapper.updateRefreshToken(reissuedToken);

        return tokenDTO;
    }
}
