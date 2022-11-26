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
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
        UserDetails user = retrieveUser(authentication.getName());

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
        return customUserDetailService.loadUserByUsername(userId);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
