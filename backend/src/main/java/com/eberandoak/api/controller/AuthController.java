package com.eberandoak.api.controller;

import com.eberandoak.api.dto.*;
import com.eberandoak.api.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController @RequestMapping("/api/auth")
public class AuthController {
    private final AuthService auth;
    public AuthController(AuthService auth){this.auth=auth;}
    @PostMapping("/register") public AuthResponse register(@Valid @RequestBody RegisterRequest body){return auth.register(body);}
    @PostMapping("/login") public AuthResponse login(@Valid @RequestBody LoginRequest body){return auth.login(body);}
    @GetMapping("/me") public UserView me(@AuthenticationPrincipal UserDetails principal){return auth.current(principal.getUsername());}
    @PostMapping("/logout") public org.springframework.http.ResponseEntity<Void> logout(){return org.springframework.http.ResponseEntity.noContent().build();}
}
