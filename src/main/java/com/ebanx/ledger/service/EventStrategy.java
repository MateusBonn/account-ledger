package com.ebanx.ledger.service;

import com.ebanx.ledger.dto.request.EventRequest;
import com.ebanx.ledger.dto.response.EventResponse;
import org.springframework.stereotype.Component;

@Component
public interface EventStrategy {

  EventResponse execute(EventRequest request);
  String getEventType();
}
