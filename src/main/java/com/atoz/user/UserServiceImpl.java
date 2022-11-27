package com.atoz.user;

import com.atoz.authentication.entity.Authority;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Slf4j
@Service
public class UserServiceImpl implements UserService, UserDetailsService {

    @Autowired
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public UserResponseDTO signup(SignupDTO signupDTO) {
        Set<Authority> authorities = new HashSet<>();
        authorities.add(Authority.ROLE_USER);

        UserEntity userEntity = new UserEntity(passwordEncoder, signupDTO, authorities);
        userMapper.addUser(userEntity);
        userMapper.addAuthority(userEntity);

        return new UserResponseDTO(signupDTO);
    }

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
                .map(auth -> auth.name())
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        return new User(
                member.getUserId(),
                member.getPassword(),
                authList
        );
    }
}
