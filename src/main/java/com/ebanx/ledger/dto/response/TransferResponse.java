package com.ebanx.ledger.dto.response;

import com.ebanx.ledger.model.Account;

public record TransferResponse(Account origin, Account destination) {}