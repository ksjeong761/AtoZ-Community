package com.atoz.authentication.help;

import com.atoz.user.entity.Authority;
import com.atoz.user.entity.UserEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.*;
import java.util.stream.Collectors;

public class StubUserDetailsService implements UserDetailsService {

    UserEntity signedUpUser = UserEntity.builder()
            .userId("testUserId")
            .password("testPassword")
            .nickname("testNickname")
            .email("test@test.com")
            .authorities(Set.of(Authority.ROLE_USER))
            .build();

    private final List<UserEntity> users = List.of(signedUpUser);

    @Override
    public UserDetails loadUserByUsername(String targetUserId) throws UsernameNotFoundException {
        for (var user : users) {
            if (user.getUserId().equals(targetUserId)) {
                List<SimpleGrantedAuthority> grantedAuthorities = user.getAuthorities()
                        .stream()
                        .map(Enum::name)
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

                return new User(user.getUserId(), user.getPassword(), grantedAuthorities);
            }
        }

        throw new UsernameNotFoundException("해당 유저가 존재하지 않습니다.");
    }
}
