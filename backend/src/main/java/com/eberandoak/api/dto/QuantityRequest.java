package com.eberandoak.api.dto;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
public record QuantityRequest(@Min(1) @Max(99) int quantity) {}
