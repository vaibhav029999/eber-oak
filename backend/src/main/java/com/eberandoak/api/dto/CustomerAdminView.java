package com.eberandoak.api.dto;
import java.time.Instant;
public record CustomerAdminView(Long id,String name,String email,String phone,Instant createdAt,long orderCount) {}
