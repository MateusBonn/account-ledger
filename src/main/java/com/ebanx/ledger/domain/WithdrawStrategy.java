package com.ebanx.ledger.domain;

import com.ebanx.ledger.adapters.inbound.dto.request.EventRequest;
import com.ebanx.ledger.adapters.inbound.dto.response.EventResponse;
import com.ebanx.ledger.adapters.inbound.exception.AccountNotFoundException;
import com.ebanx.ledger.adapters.outbound.repository.InMemoryAccountRepository;
import com.ebanx.ledger.domain.model.Account;
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
