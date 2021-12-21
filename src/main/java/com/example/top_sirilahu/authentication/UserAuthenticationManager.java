package com.example.top_sirilahu.authentication;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

public class UserAuthenticationManager implements AuthenticationManager
{

    private final UserAuthenticationProvider authProvider;

    public UserAuthenticationManager(UserAuthenticationProvider authProvider) {
        this.authProvider = authProvider;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        Authentication verifiedAuthentication = authProvider.authenticate(authentication);
        return verifiedAuthentication;
    }
}
