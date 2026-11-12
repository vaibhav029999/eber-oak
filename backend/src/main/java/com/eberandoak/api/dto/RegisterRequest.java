package com.eberandoak.api.dto;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
public record RegisterRequest(@NotBlank @Size(min=2,max=100) String name,@NotBlank @Email @Size(max=190) String email,@NotBlank @Size(min=8,max=72) String password,@NotBlank @Pattern(regexp="^[+0-9() .-]{8,30}$",message="Enter a valid phone number") String phone) {}
