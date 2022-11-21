package com.atoz.authentication.help;

import com.atoz.authentication.entity.Authority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class StubCustomUserDetailService implements UserDetailsService {

    private PasswordEncoder passwordEncoder = new Argon2PasswordEncoder();

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (username.equals("testId")) {
            String password = passwordEncoder.encode("testPassword");

            Set<Authority> authorities = new HashSet<>();
            authorities.add(Authority.ROLE_USER);
            List<SimpleGrantedAuthority> authList = authorities.stream()
                    .map(auth -> auth.name())
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());

            return new User(username, password, authList);
        } else {
            throw new UsernameNotFoundException("해당 유저가 존재하지 않습니다.");
        }
    }
}
