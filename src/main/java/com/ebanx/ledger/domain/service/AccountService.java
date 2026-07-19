package com.ebanx.ledger.domain.service;

import com.ebanx.ledger.adapters.inbound.dto.request.EventRequest;
import com.ebanx.ledger.adapters.inbound.dto.response.DepositResponse;
import com.ebanx.ledger.adapters.outbound.repository.InMemoryAccountRepository;
import com.ebanx.ledger.domain.model.Account;
import org.springframework.stereotype.Service;

@Service
public class AccountService {

  private final InMemoryAccountRepository repository;

  public AccountService(InMemoryAccountRepository repository) {
    this.repository = repository;
  }

  public DepositResponse executeDeposit(EventRequest request) {
    Account updatedAccount = repository.deposit(request.destination(), request.amount());
    return new DepositResponse(updatedAccount);
  }
}
