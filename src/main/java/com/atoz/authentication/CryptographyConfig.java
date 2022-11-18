package com.atoz.authentication;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class CryptographyConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        String idForEncode = "argon2";
        Map encoders = new HashMap<>();
        encoders.put(idForEncode, new Argon2PasswordEncoder());

        return new DelegatingPasswordEncoder(idForEncode, encoders);
    }
}
