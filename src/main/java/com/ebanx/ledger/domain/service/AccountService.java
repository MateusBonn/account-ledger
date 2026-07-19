package com.ebanx.ledger.domain.service;

import com.ebanx.ledger.adapters.inbound.dto.request.EventRequest;
import com.ebanx.ledger.adapters.inbound.dto.response.EventResponse;
import com.ebanx.ledger.domain.EventStrategy;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class AccountService {

  private final Map<String, EventStrategy> strategies;

  public AccountService(List<EventStrategy> strategyList) {
    this.strategies = strategyList.stream()
      .collect(Collectors.toMap(EventStrategy::getEventType, s -> s));
  }

  public EventResponse processEvent(EventRequest request) {
    EventStrategy strategy = strategies.get(request.type());
    if (strategy == null) {
      throw new IllegalArgumentException("Unknown event type: " + request.type());
    }
    return strategy.execute(request);
  }

}
