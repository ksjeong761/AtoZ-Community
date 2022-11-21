package com.atoz.authentication.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class RefreshToken {
    private String tokenKey;
    private String tokenValue;

    public void updateValue(String tokenValue) {
        this.tokenValue = tokenValue;
    }

    @Builder
    public RefreshToken(String tokenKey, String tokenValue) {
        this.tokenKey = tokenKey;
        this.tokenValue = tokenValue;
    }
}
