package com.atoz.authentication.testDouble;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.authority.mapping.NullAuthoritiesMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class TestDoubleCustomUserIdPasswordAuthProvider implements AuthenticationProvider {

    private PasswordEncoder passwordEncoder = new Argon2PasswordEncoder();
    private GrantedAuthoritiesMapper authoritiesMapper = new NullAuthoritiesMapper();

    protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken authentication) {
        String presentedPassword = authentication.getCredentials().toString();

        if (!this.passwordEncoder.matches(presentedPassword, userDetails.getPassword())) {
            throw new BadCredentialsException("패스워드가 틀립니다.");
        }
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        TestDoubleCustomUserDetailService testDoubleCustomUserDetailService = new TestDoubleCustomUserDetailService();
        UserDetails user = testDoubleCustomUserDetailService.loadUserByUsername(authentication.getName());

        Object principal = user;

        UsernamePasswordAuthenticationToken result = new UsernamePasswordAuthenticationToken(
                principal,
                authentication.getCredentials(),
                this.authoritiesMapper.mapAuthorities(user.getAuthorities())
        );

        this.additionalAuthenticationChecks(user, result);
        result.setDetails(authentication.getDetails());

        return result;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return false;
    }
}