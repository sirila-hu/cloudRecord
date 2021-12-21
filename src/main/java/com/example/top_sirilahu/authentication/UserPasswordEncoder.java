package com.example.top_sirilahu.authentication;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component("UserPasswordEncoder")
public class UserPasswordEncoder implements PasswordEncoder
{
    @Override
    public String encode(CharSequence charSequence) {
        return charSequence.toString();
    }

    @Override
    public boolean matches(CharSequence charSequence, String s) {
        if (s.equals(charSequence.toString()))
        {
            return true;
        }
        return false;
    }
}
