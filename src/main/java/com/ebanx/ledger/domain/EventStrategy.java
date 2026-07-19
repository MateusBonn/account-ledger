package com.ebanx.ledger.domain;

import com.ebanx.ledger.adapters.inbound.dto.request.EventRequest;
import com.ebanx.ledger.adapters.inbound.dto.response.EventResponse;
import org.springframework.stereotype.Component;

@Component
public interface EventStrategy {

  EventResponse execute(EventRequest request);
  String getEventType();
}
