package com.eberandoak.api.dto;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
public record FoodRequest(@NotNull Long restaurantId,@NotNull Long categoryId,@NotBlank @Size(max=120) String name,@Size(max=500) String description,@NotBlank @Size(max=1000) String imageUrl,@NotNull @DecimalMin("0.01") BigDecimal price,boolean vegetarian,boolean spicy,boolean available) {}
