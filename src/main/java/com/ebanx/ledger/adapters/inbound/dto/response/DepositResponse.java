package com.ebanx.ledger.adapters.inbound.dto.response;

import com.ebanx.ledger.domain.model.Account;

public record DepositResponse(Account destination) {}
