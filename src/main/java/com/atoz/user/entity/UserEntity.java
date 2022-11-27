package com.atoz.user.entity;

import com.atoz.user.dto.SignupDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Set;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity {

    private String userId;

    private String password;

    private String nickname;

    private String email;

    private Set<Authority> authorities;

    public UserEntity(PasswordEncoder passwordEncoder, SignupDTO signupDTO, Set<Authority> authorities) {
        String password = passwordEncoder.encode(signupDTO.getPassword());

        this.userId = signupDTO.getUserId();
        this.password = password;
        this.email = signupDTO.getEmail();
        this.nickname = signupDTO.getNickname();
        this.authorities = authorities;
    }

    public UserDetails toUserDetails() {
        List<SimpleGrantedAuthority> authorities = getAuthorities()
                .stream()
                .map(Enum::name)
                .map(SimpleGrantedAuthority::new)
                .toList();

        return User.builder()
                .username(getUserId())
                .authorities(authorities)
                .password(getPassword())
                .build();
    }
}
