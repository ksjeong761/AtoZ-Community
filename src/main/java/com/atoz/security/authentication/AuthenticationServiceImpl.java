package com.atoz.security.authentication;

import com.atoz.user.Authority;
import com.atoz.security.token.TokenManager;
import com.atoz.security.authentication.dto.request.TokenRequestDto;
import com.atoz.security.token.dto.RefreshTokenDto;
import com.atoz.security.token.RefreshTokenMapper;
import com.atoz.security.authentication.dto.response.TokenResponseDto;
import com.atoz.security.authentication.dto.request.SigninRequestDto;
import com.atoz.user.dto.UserDto;
import com.atoz.user.UserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
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
    public TokenResponseDto signin(SigninRequestDto signinRequestDto) {
        Authentication authentication = new UsernamePasswordAuthenticationToken(signinRequestDto.getUserId(), signinRequestDto.getPassword());
        authenticationManager.authenticate(authentication);

        return updateTokens(signinRequestDto.getUserId());
    }

    @Override
    @Transactional
    public void signout() {
        refreshTokenMapper.deleteToken(loadUserIdFromContext());
    }

    @Override
    @Transactional
    public TokenResponseDto refresh(TokenRequestDto tokenRequestDto) {
        tokenManager.validateToken(tokenRequestDto.getAccessToken());
        tokenManager.validateToken(tokenRequestDto.getRefreshToken());

        String userId = tokenManager.parseUserId(tokenRequestDto.getRefreshToken());
        return updateTokens(userId);
    }

    private TokenResponseDto updateTokens(String userId) {
        Set<Authority> authorities = findUserById(userId).getAuthorities();

        String accessToken = tokenManager.createAccessToken(userId, authorities);
        String refreshToken = tokenManager.createRefreshToken(userId, authorities);

        saveOrUpdateRefreshToken(userId, refreshToken);

        return TokenResponseDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    private UserDto findUserById(String userId) {
        return userMapper.findUserByUserId(userId)
                .orElseThrow(() -> new UsernameNotFoundException("해당 유저가 존재하지 않습니다."));
    }

    private void saveOrUpdateRefreshToken(String userId, String refreshToken) {
        RefreshTokenDto oldRefreshTokenDto = refreshTokenMapper.findTokenByKey(userId).orElse(null);
        RefreshTokenDto newRefreshTokenDto = RefreshTokenDto.builder()
                .tokenKey(userId)
                .tokenValue(refreshToken)
                .build();

        if (oldRefreshTokenDto == null) {
            refreshTokenMapper.saveToken(newRefreshTokenDto);
        } else {
            refreshTokenMapper.updateToken(newRefreshTokenDto);
        }
    }

    private String loadUserIdFromContext() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userDetails.getUsername();
    }
}