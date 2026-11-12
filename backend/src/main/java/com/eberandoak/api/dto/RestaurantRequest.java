package com.eberandoak.api.dto;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
public record RestaurantRequest(@NotBlank @Size(max=120) String name,@NotBlank @Size(max=100) String cuisine,@Size(max=500) String description,@NotBlank @Size(max=1000) String imageUrl,@NotNull @DecimalMin("0.0") @DecimalMax("5.0") BigDecimal rating,@NotNull @Min(1) Integer deliveryMinutes,@NotNull @DecimalMin("0.0") BigDecimal deliveryFee,@NotNull @DecimalMin("0.0") BigDecimal minimumOrder,@Size(max=10) String priceLevel,@Size(max=100) String offer,boolean featured,boolean active) {}
