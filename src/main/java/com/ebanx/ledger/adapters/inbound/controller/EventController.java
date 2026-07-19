package com.ebanx.ledger.adapters.inbound.controller;

import com.ebanx.ledger.adapters.inbound.dto.request.EventRequest;
import com.ebanx.ledger.adapters.inbound.dto.response.EventResponse;
import com.ebanx.ledger.domain.service.AccountService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EventController {

  private final AccountService accountService;

  public EventController(AccountService accountService) {
    this.accountService = accountService;
  }

  @GetMapping("/balance")
  public ResponseEntity<Integer> getBalance(@RequestParam("account_id") String accountId) {
    return ResponseEntity.ok(accountService.getBalance(accountId));
  }

  @PostMapping("/event")
  public ResponseEntity<EventResponse> handleEvent(@Valid @RequestBody EventRequest payload) {
    EventResponse response = accountService.processEvent(payload);
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  @PostMapping("/reset")
  public ResponseEntity<Void> reset() {
    accountService.reset();
    return ResponseEntity.status(HttpStatus.OK).build();
  }
}
