package com.eberandoak.api.dto;
import java.math.BigDecimal;
public record FoodView(Long id,Long restaurantId,String restaurantName,Long categoryId,String categoryName,String name,String description,String imageUrl,BigDecimal price,boolean vegetarian,boolean spicy,boolean available) {}
