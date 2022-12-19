package com.atoz.security.authorization;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class OwnerAuthorizationProvider {

    public boolean isOwnResource(String resourceOwner) {
        return resourceOwner.equals(loadUserIdFromContext());
    }

    private String loadUserIdFromContext() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userDetails.getUsername();
    }
}
