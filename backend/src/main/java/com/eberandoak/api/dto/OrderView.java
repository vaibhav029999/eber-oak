package com.eberandoak.api.dto;
import com.eberandoak.api.entity.OrderStatus;
import com.eberandoak.api.entity.PaymentMethod;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
public record OrderView(Long id,String restaurantName,String customerName,String phone,String deliveryAddress,BigDecimal subtotal,BigDecimal deliveryFee,BigDecimal discountAmount,BigDecimal totalAmount,PaymentMethod paymentMethod,OrderStatus status,String couponCode,Instant createdAt,Integer estimatedDeliveryMinutes,List<OrderItemView> items) {}
