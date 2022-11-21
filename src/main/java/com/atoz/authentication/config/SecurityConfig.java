package com.atoz.authentication.config;

import com.atoz.authentication.token.CustomUserIdPasswordAuthProvider;
import com.atoz.authentication.token.JwtAccessDeniedHandler;
import com.atoz.authentication.token.JwtAuthenticationEntryPoint;
import com.atoz.authentication.token.JwtFilter;
import com.atoz.authentication.mapper.RefreshTokenMapper;
import com.atoz.authentication.token.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@RequiredArgsConstructor
@Configuration
public class SecurityConfig {
    private final TokenProvider tokenProvider;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
    private final CustomUserIdPasswordAuthProvider customUserIdPasswordAuthProvider;
    private final RefreshTokenMapper refreshTokenMapper;

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder =
                http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.authenticationProvider(customUserIdPasswordAuthProvider);
        return authenticationManagerBuilder.build();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return web -> web.ignoring().antMatchers("/h2-console/**", "/favicon.ico");
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .exceptionHandling()
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .accessDeniedHandler(jwtAccessDeniedHandler)
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers("/auth/**", "/user/**").permitAll()
                .antMatchers("v3/api-docs", "/configuration/**", "/wagger*/**", "/webjars/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .addFilterBefore(new JwtFilter(tokenProvider, refreshTokenMapper), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

}
