package com.atoz.user.helper;

import com.atoz.user.dto.UserDto;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.*;
import java.util.stream.Collectors;

public class MockUserDetailsService implements UserDetailsService {

    private final List<UserDto> users;

    public MockUserDetailsService(List<UserDto> users) {
        this.users = users;
    }


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
