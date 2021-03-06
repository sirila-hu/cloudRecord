package com.example.top_sirilahu.service;

import com.example.top_sirilahu.entity.userEntity;
import com.example.top_sirilahu.repository.userRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthenticatService implements UserDetailsService
{
    userRepository userRepo;

    public AuthenticatService() {
    }

    @Autowired
    public AuthenticatService(userRepository userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        UserDetails user = userRepo.findByUsername(s);
        if (user != null)
        {
            return user;
        }
        else
        {
            throw new UsernameNotFoundException("该账户不存在");
        }
    }

    public UserDetails loadUserByUID(long UID) throws UsernameNotFoundException {
        Optional<userEntity> optional = userRepo.findById(UID);
        if (optional.isPresent())
        {
            return optional.get();
        }
        else
        {
            throw new UsernameNotFoundException("该账户不存在");
        }
    }
}
