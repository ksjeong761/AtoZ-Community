package com.atoz.authentication.help;


import com.atoz.user.entity.Authority;
import com.atoz.security.authentication.dto.TokenRequestDTO;
import com.atoz.security.token.RefreshTokenEntity;
import com.atoz.security.token.RefreshTokenMapper;
import com.atoz.security.authentication.dto.TokenResponseDTO;
import com.atoz.security.authentication.AuthenticationService;
import com.atoz.security.token.TokenProvider;
import com.atoz.error.exception.InvalidTokenException;
import com.atoz.user.dto.SigninDTO;
import com.atoz.user.entity.UserEntity;
import com.atoz.user.help.SpyStubUserMapper;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Set;

public class StubAuthenticationService implements AuthenticationService {
    private String secretKey = "b3VyLXByb2plY3QtbmFtZS1BdG9aLWxpa2UtYmxpbmQtZm9yLWdlbmVyYXRpb24tb3VyLXByb2plY3QtbGlrZS1ibGluZC1nZW5lcmF0aW9u";
    private StubCustomUserIdPasswordAuthProvider authenticationManager =
            new StubCustomUserIdPasswordAuthProvider();
    private StubCustomUserDetailService customUserDetailService;
    private TokenProvider tokenProvider = new TokenProvider(secretKey, 1800000, 604800000);
    private RefreshTokenMapper refreshTokenMapper = new StubRefreshTokenMapper();
    private SpyStubUserMapper userMapper = new SpyStubUserMapper();

    @Override
    public TokenResponseDTO signin(SigninDTO signinDTO) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(
                        signinDTO.getUserId(), signinDTO.getPassword());

        Authentication authenticate = authenticationManager
                .authenticate(authenticationToken);
        String userId = authenticate.getName();

        customUserDetailService = new StubCustomUserDetailService();

        UserEntity user = userMapper.findById(userId).orElseThrow(() -> new UsernameNotFoundException("해당 유저가 존재하지 않습니다."));

        Set<Authority> authorities = user.getAuthorities();

        String accessToken = tokenProvider.createAccessToken(signinDTO.getUserId(), authorities);
        String refreshToken = tokenProvider.createRefreshToken(userId, authorities);

        refreshTokenMapper.saveToken(RefreshTokenEntity.builder()
                .tokenKey(userId)
                .tokenValue(refreshToken)
                .build());

        return tokenProvider.createTokenDTO(accessToken, refreshToken);
    }

    @Override
    public void signout(TokenRequestDTO tokenRequestDTO) {
        String accessToken = tokenRequestDTO.getAccessToken();
        tokenProvider.validateToken(accessToken);
        Authentication authentication = tokenProvider.getAuthentication(accessToken);

        refreshTokenMapper.findTokenByKey(authentication.getName()).orElseThrow(() -> new InvalidTokenException("인증정보가 없습니다."));

        refreshTokenMapper.deleteToken(authentication.getName());
    }

    @Override
    public TokenResponseDTO refresh(TokenRequestDTO tokenRequestDTO) {
        String orgAccessToken = tokenRequestDTO.getAccessToken();
        String orgRefreshToken = tokenRequestDTO.getRefreshToken();

        Authentication authentication = tokenProvider.getAuthentication(orgAccessToken);

        String userId = tokenProvider.getUserIdByToken(orgAccessToken);

        UserEntity user = userMapper.findById(userId).orElseThrow(() -> new UsernameNotFoundException("해당 유저가 존재하지 않습니다."));

        Set<Authority> authorities = user.getAuthorities();

        String newAccessToken = tokenProvider.createAccessToken(userId, authorities);
        String newRefreshToken = tokenProvider.createRefreshToken(userId, authorities);
        TokenResponseDTO tokenResponseDTO = tokenProvider.createTokenDTO(newAccessToken, newRefreshToken);

        RefreshTokenEntity saveRefreshTokenEntity = RefreshTokenEntity.builder()
                .tokenKey(userId)
                .tokenValue(newRefreshToken)
                .build();

        refreshTokenMapper.saveToken(saveRefreshTokenEntity);

        return tokenResponseDTO;
    }
}