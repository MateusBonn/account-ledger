package com.ebanx.ledger.adapters.inbound.controller;

import com.ebanx.ledger.adapters.outbound.repository.InMemoryAccountRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/balance")
public class BalanceController {

  private final InMemoryAccountRepository repository;

  public BalanceController(InMemoryAccountRepository repository) {
    this.repository = repository;
  }

  @GetMapping
  public ResponseEntity<Integer> getBalance(@RequestParam("account_id") String accountId) {
    return repository.findById(accountId)
      .map(account -> ResponseEntity.ok(account.balance()))
      .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(0));
  }
}
