package com.eberandoak.api.dto;
import java.math.BigDecimal;
import java.util.List;
public record CartView(List<CartItemView> items,BigDecimal subtotal,BigDecimal deliveryFee,BigDecimal discountAmount,Long restaurantId,String restaurantName) {}
