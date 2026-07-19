package com.ebanx.ledger.integration;

import com.ebanx.ledger.dto.request.EventRequest;
import com.ebanx.ledger.repository.InMemoryAccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class LedgerE2ETest {

  @Autowired
  private TestRestTemplate restTemplate;

  @Autowired
  private InMemoryAccountRepository repository;

  @BeforeEach
  void setUp() {
    repository.clear();
  }

  @Test
  @DisplayName("E2E: Deve realizar fluxo completo de depósito, saque e transferência")
  void shouldPerformFullFlow() {
    // 1. Depósito
    EventRequest deposit = new EventRequest("deposit", null, "100", 20);
    ResponseEntity<Object> depositRes = restTemplate.postForEntity("/event", deposit, Object.class);
    assertThat(depositRes.getStatusCode()).isEqualTo(HttpStatus.CREATED);

    // 2. Verifica Saldo
    ResponseEntity<Integer> balanceRes = restTemplate.getForEntity("/balance?account_id=100", Integer.class);
    assertThat(balanceRes.getBody()).isEqualTo(20);

    // 3. Saque
    EventRequest withdraw = new EventRequest("withdraw", "100", null, 5);
    ResponseEntity<Object> withdrawRes = restTemplate.postForEntity("/event", withdraw, Object.class);
    assertThat(withdrawRes.getStatusCode()).isEqualTo(HttpStatus.CREATED);

    // 4. Transferência
    EventRequest transfer = new EventRequest("transfer", "100", "200", 10);
    ResponseEntity<Object> transferRes = restTemplate.postForEntity("/event", transfer, Object.class);
    assertThat(transferRes.getStatusCode()).isEqualTo(HttpStatus.CREATED);

    // 5. Verifica saldos finais
    assertThat(restTemplate.getForEntity("/balance?account_id=100", Integer.class).getBody()).isEqualTo(5);
    assertThat(restTemplate.getForEntity("/balance?account_id=200", Integer.class).getBody()).isEqualTo(10);
  }

  @Test
  @DisplayName("E2E: Deve retornar 404 para saldo de conta inexistente")
  void shouldReturn404ForNonExistentAccount() {
    ResponseEntity<Integer> response = restTemplate.getForEntity("/balance?account_id=999", Integer.class);
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    assertThat(response.getBody()).isEqualTo(0);
  }

  @Test
  @DisplayName("E2E: Deve retornar 422 para saque com saldo insuficiente")
  void shouldReturn422ForInsufficientFunds() {
    // Cria conta com 10
    restTemplate.postForEntity("/event", new EventRequest("deposit", null, "1", 10), Object.class);

    // Tenta sacar 20
    EventRequest withdraw = new EventRequest("withdraw", "1", null, 20);
    ResponseEntity<Object> response = restTemplate.postForEntity("/event", withdraw, Object.class);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY);
    assertThat(response.getBody()).isEqualTo(0);
  }
}