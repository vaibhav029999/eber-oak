package com.eberandoak.api.security;

import com.eberandoak.api.entity.UserEntity;
import com.eberandoak.api.repository.UserRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class DatabaseUserDetailsService implements UserDetailsService {
    private final UserRepository users;
    public DatabaseUserDetailsService(UserRepository users) { this.users=users; }
    @Override public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserEntity entity=users.findByEmailIgnoreCase(email).orElseThrow(()->new UsernameNotFoundException("Account not found"));
        return User.withUsername(entity.getEmail()).password(entity.getPasswordHash()).roles(entity.getRole().name()).build();
    }
}
