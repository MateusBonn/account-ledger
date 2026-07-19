package com.ebanx.ledger.dto.response;

import com.ebanx.ledger.model.Account;

public record DepositResponse(Account destination) {}
