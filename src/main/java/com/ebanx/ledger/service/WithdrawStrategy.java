package com.ebanx.ledger.service;

import com.ebanx.ledger.dto.request.EventRequest;
import com.ebanx.ledger.dto.response.EventResponse;
import com.ebanx.ledger.exception.AccountNotFoundException;
import com.ebanx.ledger.repository.InMemoryAccountRepository;
import com.ebanx.ledger.model.Account;
import org.springframework.stereotype.Component;

@Component
public class WithdrawStrategy implements EventStrategy {
  private final InMemoryAccountRepository repository;

  public WithdrawStrategy(InMemoryAccountRepository repository) {
    this.repository = repository;
  }

  @Override
  public String getEventType() {
    return "withdraw";
  }

  @Override
  public EventResponse execute(EventRequest request) {
    Account origin = repository.withdraw(request.origin(), request.amount());

    if (origin == null) throw new AccountNotFoundException(request.origin());

    return EventResponse.fromWithdraw(origin);
  }


}
