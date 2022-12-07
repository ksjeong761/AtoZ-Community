package com.atoz.security.authentication;

import com.atoz.security.authentication.dto.SignoutDTO;
import com.atoz.user.entity.Authority;
import com.atoz.security.token.TokenManager;
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
    private final TokenManager tokenManager;

    @Override
    @Transactional
    public TokenResponseDTO signin(SigninDTO signinDTO) {
        Authentication authentication = new UsernamePasswordAuthenticationToken(signinDTO.getUserId(), signinDTO.getPassword());
        authenticationManager.authenticate(authentication);

        return updateTokens(signinDTO.getUserId());
    }

    @Override
    @Transactional
    public void signout(SignoutDTO signoutDTO) {
        refreshTokenMapper.deleteToken(signoutDTO.getUserId());
    }

    @Override
    @Transactional
    public TokenResponseDTO refresh(TokenRequestDTO tokenRequestDTO) {
        tokenManager.validateToken(tokenRequestDTO.getAccessToken());
        tokenManager.validateToken(tokenRequestDTO.getRefreshToken());

        String userId = tokenManager.parseUserId(tokenRequestDTO.getRefreshToken());
        return updateTokens(userId);
    }

    private TokenResponseDTO updateTokens(String userId) {
        Set<Authority> authorities = findUserById(userId).getAuthorities();

        String accessToken = tokenManager.createAccessToken(userId, authorities);
        String refreshToken = tokenManager.createRefreshToken(userId, authorities);

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