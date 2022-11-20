package com.atoz.authentication;

public enum Authority {
    ROLE_USER("ROLE_USER"),
    ROLE_ADMIN("ROLE_ADMIN");

    private final String authorityName;

    Authority(String authorityName) {
        this.authorityName = authorityName;
    }

    public String getAuthorityName() {
        return this.authorityName;
    }
}
