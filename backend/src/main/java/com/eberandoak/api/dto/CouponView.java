package com.eberandoak.api.dto;
import java.math.BigDecimal;
public record CouponView(Long id,String code,Integer discountPercent,BigDecimal minimumAmount,boolean active) {}
