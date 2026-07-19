package com.ebanx.ledger.adapters.inbound.controller;

import com.ebanx.ledger.adapters.inbound.dto.request.EventRequest;
import com.ebanx.ledger.adapters.inbound.dto.response.DepositResponse;
import com.ebanx.ledger.domain.service.AccountService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/event")
public class EventController {

  private final AccountService accountService;

  public EventController(AccountService accountService) {
    this.accountService = accountService;
  }

  @PostMapping
  public ResponseEntity<?> handleEvent(@RequestBody EventRequest payload) {
    if ("deposit".equals(payload.type())) {
      DepositResponse response = accountService.executeDeposit(payload);
      return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(0);
  }
}
