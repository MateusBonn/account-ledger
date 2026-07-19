package com.ebanx.ledger.service;

import com.ebanx.ledger.dto.request.EventRequest;
import com.ebanx.ledger.dto.response.EventResponse;
import com.ebanx.ledger.repository.InMemoryAccountRepository;
import com.ebanx.ledger.model.Account;
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
