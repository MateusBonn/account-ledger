package com.ebanx.ledger.domain;

import com.ebanx.ledger.adapters.inbound.dto.request.EventRequest;
import com.ebanx.ledger.adapters.inbound.dto.response.EventResponse;
import com.ebanx.ledger.adapters.outbound.repository.InMemoryAccountRepository;
import com.ebanx.ledger.domain.model.Account;
import org.springframework.stereotype.Component;

@Component
public class DepositStrategy implements EventStrategy {
  private final InMemoryAccountRepository repository;

  public DepositStrategy(InMemoryAccountRepository repository) {
    this.repository = repository;
  }

  @Override
  public String getEventType() { return "deposit"; }


  @Override
  public EventResponse execute(EventRequest request) {
    Account deposit = repository.deposit(request.destination(), request.amount());
    return EventResponse.fromDeposit(deposit);
  }


}
