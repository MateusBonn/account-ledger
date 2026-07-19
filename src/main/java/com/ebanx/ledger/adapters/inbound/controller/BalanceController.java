package com.ebanx.ledger.adapters.inbound.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/balance")
public class BalanceController {

  @GetMapping("/balance")
  public ResponseEntity<Integer> getBalance(@RequestParam("account_id") String accountId) {
    // Todo: Substituir por chamada ao Service/Store na Fase 3.
    // Retorno fixo 404 com body 0 para garantir que a rota existe e falha graciosamente.
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(0);
  }
}
