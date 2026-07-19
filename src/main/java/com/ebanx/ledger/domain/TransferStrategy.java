package com.ebanx.ledger.domain;

import com.ebanx.ledger.adapters.inbound.dto.request.EventRequest;
import com.ebanx.ledger.adapters.inbound.dto.response.EventResponse;
import com.ebanx.ledger.adapters.inbound.exception.AccountNotFoundException;
import com.ebanx.ledger.adapters.outbound.repository.InMemoryAccountRepository;
import org.springframework.stereotype.Component;

@Component
public class TransferStrategy implements EventStrategy {
  private final InMemoryAccountRepository repository;

  public TransferStrategy(InMemoryAccountRepository repository) {
    this.repository = repository;
  }

  @Override
  public String getEventType() {
    return "transfer";
  }

  @Override
  public EventResponse execute(EventRequest request) {
    var transfer = repository.transfer(request.origin(), request.destination(), request.amount());
    if (transfer == null) throw new AccountNotFoundException(request.origin());
    return EventResponse.fromTransfer(transfer.origin(), transfer.destination());
  }


}
