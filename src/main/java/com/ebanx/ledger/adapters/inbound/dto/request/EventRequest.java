package com.ebanx.ledger.adapters.inbound.dto.request;

public record EventRequest(
  String type,
  String origin,
  String destination,
  int amount
) {}
