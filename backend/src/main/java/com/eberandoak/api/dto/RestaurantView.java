package com.eberandoak.api.dto;
import java.math.BigDecimal;
public record RestaurantView(Long id,String name,String cuisine,String description,String imageUrl,BigDecimal rating,Integer deliveryMinutes,BigDecimal deliveryFee,BigDecimal minimumOrder,String priceLevel,String offer,boolean featured,boolean active) {}
