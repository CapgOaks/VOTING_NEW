package com.capgemini.security4.security;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

public class SecurityUtils {

    
    private SecurityUtils() {
        throw new UnsupportedOperationException("Utility class should not be instantiated");
    }

    public static UserDetails getCurrentUser() {
        return (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
