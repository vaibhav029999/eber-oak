package com.eberandoak.api.dto;
import com.eberandoak.api.entity.OrderStatus;
import jakarta.validation.constraints.NotNull;
public record OrderStatusRequest(@NotNull OrderStatus status) {}
