package com.ebanx.ledger.service;

import com.ebanx.ledger.dto.request.EventRequest;
import com.ebanx.ledger.dto.response.EventResponse;
import com.ebanx.ledger.exception.AccountNotFoundException;
import com.ebanx.ledger.repository.InMemoryAccountRepository;
import com.ebanx.ledger.model.Account;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class AccountService {

  private final Map<String, EventStrategy> strategies;
  private final InMemoryAccountRepository repository;

  public AccountService(List<EventStrategy> strategyList, InMemoryAccountRepository repository) {
    this.strategies = strategyList.stream()
      .collect(Collectors.toMap(EventStrategy::getEventType, s -> s));
    this.repository = repository;
  }

  public Integer getBalance(String accountId) {
    return repository.findById(accountId)
      .map(Account::balance)
      .orElseThrow(() -> new AccountNotFoundException(accountId));
  }

  public EventResponse processEvent(EventRequest request) {
    EventStrategy strategy = strategies.get(request.type());
    if (strategy == null) {
      throw new IllegalArgumentException("Unknown event type: " + request.type());
    }
    return strategy.execute(request);
  }

  public void reset() {
    repository.clear();
  }
}
