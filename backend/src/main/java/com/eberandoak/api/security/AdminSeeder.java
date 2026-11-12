package com.eberandoak.api.security;

import com.eberandoak.api.entity.Role;
import com.eberandoak.api.entity.UserEntity;
import com.eberandoak.api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class AdminSeeder implements CommandLineRunner {
    private final UserRepository users; private final PasswordEncoder encoder;
    @Value("${app.admin.email}") private String email;
    @Value("${app.admin.password}") private String password;
    public AdminSeeder(UserRepository users,PasswordEncoder encoder){this.users=users;this.encoder=encoder;}
    @Override public void run(String... args) {
        if(users.existsByEmailIgnoreCase(email)) return;
        UserEntity admin=new UserEntity(); admin.setName("Eber & Oak Admin"); admin.setEmail(email.toLowerCase()); admin.setPasswordHash(encoder.encode(password)); admin.setRole(Role.ADMIN); users.save(admin);
    }
}
