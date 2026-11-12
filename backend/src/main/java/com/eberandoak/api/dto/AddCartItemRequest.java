package com.eberandoak.api.dto;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
public record AddCartItemRequest(@NotNull Long foodItemId,@Min(1) int quantity) {}
