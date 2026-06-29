package com.example.authify.service;

import com.example.authify.entity.UserEntity;
import com.example.authify.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class AppUserDetailService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
       UserEntity exitsUser= userRepository.findByEmail(email)
                .orElseThrow(()->new UsernameNotFoundException("Email not found for email: "+ email));
       return new User(exitsUser.getEmail(), exitsUser.getPassword(),new ArrayList<>());
    }
}
