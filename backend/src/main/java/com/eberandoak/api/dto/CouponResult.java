package com.eberandoak.api.dto;
import java.math.BigDecimal;
public record CouponResult(boolean valid,String code,Integer discountPercent,BigDecimal discountAmount,String message) {}
