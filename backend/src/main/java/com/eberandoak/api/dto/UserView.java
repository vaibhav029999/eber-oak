package com.eberandoak.api.dto;
import com.eberandoak.api.entity.Role;
import java.time.Instant;
public record UserView(Long id,String name,String email,String phone,Role role,Instant createdAt) {}
