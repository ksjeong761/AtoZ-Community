package com.atoz.user.entity;

import com.atoz.user.dto.UserResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

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

    public UserDetails toUserDetails() {
        List<SimpleGrantedAuthority> authorities = getAuthorities()
                .stream()
                .map(Enum::name)
                .map(SimpleGrantedAuthority::new)
                .toList();

        return User.builder()
                .username(getUserId())
                .password(getPassword())
                .authorities(authorities)
                .build();
    }

    public UserResponseDTO toResponseDto() {
        return UserResponseDTO.builder()
                .userId(getUserId())
                .nickname(getNickname())
                .email(getEmail())
                .build();
    }
}
