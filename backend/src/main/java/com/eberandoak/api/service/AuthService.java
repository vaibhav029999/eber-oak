package com.eberandoak.api.service;

import com.eberandoak.api.dto.*;
import com.eberandoak.api.entity.Role;
import com.eberandoak.api.entity.UserEntity;
import com.eberandoak.api.exception.ApiException;
import com.eberandoak.api.repository.UserRepository;
import com.eberandoak.api.security.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {
    private final UserRepository users; private final PasswordEncoder passwords; private final AuthenticationManager authentication; private final JwtService jwt;
    public AuthService(UserRepository users,PasswordEncoder passwords,AuthenticationManager authentication,JwtService jwt){this.users=users;this.passwords=passwords;this.authentication=authentication;this.jwt=jwt;}
    @Transactional public AuthResponse register(RegisterRequest request){
        String email=request.email().trim().toLowerCase(); if(users.existsByEmailIgnoreCase(email))throw ApiException.conflict("An account with that email already exists.");
        UserEntity u=new UserEntity();u.setName(request.name().trim());u.setEmail(email);u.setPhone(request.phone().trim());u.setPasswordHash(passwords.encode(request.password()));u.setRole(Role.USER);users.save(u);
        return new AuthResponse(jwt.createToken(email),ViewMapper.user(u));
    }
    @Transactional(readOnly=true) public AuthResponse login(LoginRequest request){
        String email=request.email().trim().toLowerCase();
        try{authentication.authenticate(new UsernamePasswordAuthenticationToken(email,request.password()));}
        catch(BadCredentialsException e){throw new ApiException(org.springframework.http.HttpStatus.UNAUTHORIZED,"That email and password don’t match. Please try again.");}
        UserEntity u=users.findByEmailIgnoreCase(email).orElseThrow(()->new ApiException(org.springframework.http.HttpStatus.UNAUTHORIZED,"That email and password don’t match. Please try again."));
        return new AuthResponse(jwt.createToken(email),ViewMapper.user(u));
    }
    @Transactional(readOnly=true) public UserView current(String email){return users.findByEmailIgnoreCase(email).map(ViewMapper::user).orElseThrow(()->ApiException.notFound("Account not found."));}
}
