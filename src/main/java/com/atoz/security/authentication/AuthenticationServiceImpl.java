package com.atoz.security.authentication;

import com.atoz.security.authorization.AuthorizationProvider;
import com.atoz.user.entity.Authority;
import com.atoz.security.token.TokenProvider;
import com.atoz.security.authentication.dto.TokenRequestDTO;
import com.atoz.security.token.RefreshTokenEntity;
import com.atoz.security.token.RefreshTokenMapper;
import com.atoz.security.authentication.dto.TokenResponseDTO;
import com.atoz.security.authentication.dto.SigninDTO;
import com.atoz.user.entity.UserEntity;
import com.atoz.user.UserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final UserMapper userMapper;
    private final RefreshTokenMapper refreshTokenMapper;
    private final TokenProvider tokenProvider;
    private final AuthorizationProvider authorizationProvider;

    @Override
    @Transactional
    public TokenResponseDTO signin(SigninDTO signinDTO) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(signinDTO.getUserId(), signinDTO.getPassword()));

        return updateTokens(authentication);
    }

    @Override
    @Transactional
    public void signout(TokenRequestDTO tokenRequestDTO) {
        String accessToken = tokenRequestDTO.getAccessToken();
        String refreshToken = tokenRequestDTO.getRefreshToken();
        Authentication authentication = authorizationProvider.authorize(accessToken, refreshToken);

        refreshTokenMapper.deleteToken(authentication.getName());
    }

    @Override
    @Transactional
    public TokenResponseDTO refresh(TokenRequestDTO tokenRequestDTO) {
        String accessToken = tokenRequestDTO.getAccessToken();
        String refreshToken = tokenRequestDTO.getRefreshToken();
        Authentication authentication = authorizationProvider.authorize(accessToken, refreshToken);

        return updateTokens(authentication);
    }

    private TokenResponseDTO updateTokens(Authentication authentication) {
        String userId = authentication.getName();
        Set<Authority> authorities = findUserById(userId).getAuthorities();
        String accessToken = tokenProvider.createAccessToken(userId, authorities);
        String refreshToken = tokenProvider.createRefreshToken(userId, authorities);

        saveOrUpdateRefreshToken(userId, refreshToken);

        return TokenResponseDTO.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    private UserEntity findUserById(String userId) {
        return userMapper.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("해당 유저가 존재하지 않습니다."));
    }

    private void saveOrUpdateRefreshToken(String userId, String refreshToken) {
        RefreshTokenEntity oldRefreshTokenEntity = refreshTokenMapper.findTokenByKey(userId).orElse(null);
        RefreshTokenEntity newRefreshTokenEntity = RefreshTokenEntity.builder()
                .tokenKey(userId)
                .tokenValue(refreshToken)
                .build();

        if (oldRefreshTokenEntity == null) {
            refreshTokenMapper.saveToken(newRefreshTokenEntity);
        } else {
            refreshTokenMapper.updateToken(newRefreshTokenEntity);
        }
    }
}