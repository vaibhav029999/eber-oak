package com.eberandoak.api.dto;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
public record CouponRequest(@NotBlank String code,@NotNull @Min(1) @Max(80) Integer discountPercent,@NotNull @Min(0) BigDecimal minimumAmount,boolean active) {}
