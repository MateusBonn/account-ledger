package com.ebanx.ledger.adapters.inbound.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/balance")
public class EventController {

  @PostMapping("/event")
  public ResponseEntity<Void> handleEvent(@RequestBody Map<String, Object> payload) {
    // Todo: Substituir por lógica de roteamento de eventos na Fase 4.
    // Utilizando Map temporariamente para evitar a criação prematura de DTOs antes da modelagem do domínio.
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }
}
