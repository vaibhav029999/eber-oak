package com.eberandoak.api.dto;
import java.math.BigDecimal;
public record CartItemView(Long id,Long foodItemId,Long restaurantId,String restaurantName,String categoryName,String foodName,String description,String imageUrl,BigDecimal price,int quantity,boolean vegetarian) {}
