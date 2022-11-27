package com.atoz.security.authentication;

import com.atoz.security.token.TokenParser;
import com.atoz.user.entity.Authority;
import com.atoz.security.token.TokenProvider;
import com.atoz.security.authentication.dto.TokenRequestDTO;
import com.atoz.security.token.RefreshTokenEntity;
import com.atoz.security.token.RefreshTokenMapper;
import com.atoz.security.authentication.dto.TokenResponseDTO;
import com.atoz.error.exception.InvalidTokenException;
import com.atoz.user.dto.SigninDTO;
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
    private final RefreshTokenMapper refreshTokenMapper;
    private final UserMapper userMapper;
    private final TokenProvider tokenProvider;
    private final TokenParser tokenParser;

    @Override
    @Transactional
    public TokenResponseDTO signin(SigninDTO signinDTO) {
        // 사용자 인증을 받는다.
        Authentication authenticate = authenticationManager.authenticate(signinDTO.toAuthentication());

        // 사용자 아이디와 권한 정보를 통해
        String userId = authenticate.getName();
        UserEntity user = getUserById(userId);
        Set<Authority> authorities = user.getAuthorities();

        // 액세스 토큰, 리프레시 토큰을 만든다.
        String accessToken = tokenProvider.createAccessToken(userId, authorities);
        String refreshToken = tokenProvider.createRefreshToken(userId, authorities);

        // 리프레시 토큰을 최신화하고
        saveOrUpdateRefreshToken(user, refreshToken);

        // 토큰을 발급한다.
        return TokenResponseDTO.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    private void saveOrUpdateRefreshToken(UserEntity user, String refreshToken) {
        RefreshTokenEntity orgRefreshTokenEntity = refreshTokenMapper.findTokenByKey(user.getUserId()).orElse(null);
        RefreshTokenEntity newRefreshTokenEntity = RefreshTokenEntity.builder()
                .tokenKey(user.getUserId())
                .tokenValue(refreshToken)
                .build();

        if (orgRefreshTokenEntity == null) {
            refreshTokenMapper.saveToken(newRefreshTokenEntity);
        } else {
            refreshTokenMapper.updateToken(newRefreshTokenEntity);
        }
    }

    @Override
    @Transactional
    public void signout(TokenRequestDTO tokenRequestDTO) {
        // 액세스 토큰으로 인가를 받는다.
        String accessToken = tokenRequestDTO.getAccessToken();
        Authentication authentication = tokenParser.toAuthentication(accessToken);

        // 리프레시 토큰을 삭제한다.
        refreshTokenMapper.findTokenByKey(authentication.getName())
                .orElseThrow(() -> new InvalidTokenException("인증정보가 없습니다."));
        refreshTokenMapper.deleteToken(authentication.getName());
    }

    @Override
    @Transactional
    public TokenResponseDTO refresh(TokenRequestDTO tokenRequestDTO) {
        // 액세스 토큰으로 인가를 받는다.
        String originAccessToken = tokenRequestDTO.getAccessToken();
        String originRefreshToken = tokenRequestDTO.getRefreshToken();
        Authentication authentication = tokenParser.toAuthentication(originAccessToken);

        RefreshTokenEntity refreshTokenEntity = refreshTokenMapper.findTokenByKey(authentication.getName())
                .orElseThrow(() -> new InvalidTokenException("로그아웃된 사용자입니다."));
        if (!refreshTokenEntity.getTokenValue().equals(originRefreshToken)) {
            throw new InvalidTokenException("토큰이 일치하지 않습니다.");
        }

        // 사용자 아이디와 권한 정보를 통해
        String userId = tokenParser.parseUserId(originAccessToken);
        UserEntity user = getUserById(userId);
        Set<Authority> authorities = user.getAuthorities();

        // 액세스 토큰, 리프레시 토큰을 만든다.
        String newAccessToken = tokenProvider.createAccessToken(userId, authorities);
        String newRefreshToken = tokenProvider.createRefreshToken(userId, authorities);

        // 리프레시 토큰을 최신화하고
        RefreshTokenEntity reissuedToken = RefreshTokenEntity.builder()
                .tokenKey(userId)
                .tokenValue(newRefreshToken)
                .build();
        refreshTokenMapper.updateToken(reissuedToken);

        // 토큰을 발급한다.
        return TokenResponseDTO.builder()
                .accessToken(newAccessToken)
                .refreshToken(newRefreshToken)
                .build();
    }

    private UserEntity getUserById(String userId) {
        return userMapper.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("해당 유저가 존재하지 않습니다."));
    }
}