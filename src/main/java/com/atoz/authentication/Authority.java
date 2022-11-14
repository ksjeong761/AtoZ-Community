package com.atoz.authentication;

import lombok.Builder;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
public class Authority {
    private MemberAuth authorityName;

    public Authority(MemberAuth authorityName) {
        this.authorityName = authorityName;
    }

    public String getAuthorityName() {
        return this.authorityName.toString();
    }
}
