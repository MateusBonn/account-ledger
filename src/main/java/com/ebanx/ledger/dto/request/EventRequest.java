package com.ebanx.ledger.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record EventRequest(
  @NotBlank(message = "Type is required")
  String type,
  String origin,
  String destination,
  @NotNull(message = "Amount is required")
  @Min(value = 0, message = "Amount must be positive")
  Integer amount
) {}
