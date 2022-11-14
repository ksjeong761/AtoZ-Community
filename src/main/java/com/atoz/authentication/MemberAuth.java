package com.atoz.authentication;

public enum MemberAuth {
    ROLE_USER("ROLE_USER"),
    ROLE_ADMIN("ROLE_ADMIN");

    private final String role;

    MemberAuth(String role) {
        this.role = role;
    }
}
