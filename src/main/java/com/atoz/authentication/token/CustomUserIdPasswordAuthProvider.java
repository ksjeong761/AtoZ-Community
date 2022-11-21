package com.atoz.authentication.token;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.authority.mapping.NullAuthoritiesMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomUserIdPasswordAuthProvider implements AuthenticationProvider {

    private final PasswordEncoder passwordEncoder;
    private final CustomUserDetailService customUserDetailService;
    private GrantedAuthoritiesMapper authoritiesMapper = new NullAuthoritiesMapper();

    protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken authentication) {
        String presentedPassword = authentication.getCredentials().toString();

        if (!this.passwordEncoder.matches(presentedPassword, userDetails.getPassword())) {
            throw new BadCredentialsException("패스워드가 틀립니다.");
        }
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        UserDetails user = null;
        try {
            user = retrieveUser(authentication.getName());
        } catch (RuntimeException e) {
            throw e;
        }

        Object principalToReturn = user;
        UsernamePasswordAuthenticationToken result =
                new UsernamePasswordAuthenticationToken(
                        principalToReturn,
                        authentication.getCredentials(),
                        this.authoritiesMapper.mapAuthorities(user.getAuthorities())
                );

        this.additionalAuthenticationChecks(user, result);
        result.setDetails(authentication.getDetails());

        return result;
    }

    protected final UserDetails retrieveUser(String userId) {
        try {
            UserDetails loadedUser = customUserDetailService.loadUserByUsername(userId);

            return loadedUser;
        } catch (Exception e) {
            throw new InternalAuthenticationServiceException(
                    "내부 인증 로직 중 알 수 없는 오류가 발생하였습니다.");
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
