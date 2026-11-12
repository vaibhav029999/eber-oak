package com.eberandoak.api.dto;
import java.math.BigDecimal;
public record OrderItemView(Long id,String name,BigDecimal price,int quantity) {}
