package com.atoz.authentication;

import com.atoz.user.UserEntity;
import com.atoz.user.UserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {

    private final RefreshTokenMapper refreshTokenMapper;
    private final UserMapper userMapper;

    /**
     * 사용자 아이디로 조회한 JwtSigninDTO를 UserDetails 타입으로 생성하여 반환
     */
    @Override
    @Transactional
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        return userMapper.findById(userId)
                .map(this::createUserDetails)
                .orElseThrow(() -> new UsernameNotFoundException("해당 유저가 존재하지 않습니다."));
    }

    private UserDetails createUserDetails(UserEntity member) {
        Set<Authority> authorities = member.getAuthorities();

        List<SimpleGrantedAuthority> authList = authorities
                .stream()
                .map(Authority::getAuthorityName)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        return new User(
                member.getUserId(),
                member.getPassword(),
                authList
        );
    }
}
