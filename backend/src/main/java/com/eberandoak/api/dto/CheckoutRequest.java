package com.eberandoak.api.dto;
import com.eberandoak.api.entity.PaymentMethod;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
public record CheckoutRequest(@NotBlank @Size(min=2,max=100) String customerName,@NotBlank @Size(min=8,max=30) String phone,@NotBlank @Size(min=8,max=255) String address,@NotBlank @Size(max=100) String city,@NotBlank @Size(min=4,max=15) String pincode,@NotNull PaymentMethod paymentMethod,String couponCode) {}
