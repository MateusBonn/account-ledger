package com.ebanx.ledger.domain;

import com.ebanx.ledger.adapters.inbound.dto.request.EventRequest;
import com.ebanx.ledger.adapters.inbound.dto.response.EventResponse;
import com.ebanx.ledger.adapters.outbound.repository.InMemoryAccountRepository;
import com.ebanx.ledger.domain.model.Account;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DepositStrategyTest {

  @Mock
  private InMemoryAccountRepository repository;

  @InjectMocks
  private DepositStrategy depositStrategy;

  @Test
  @DisplayName("getEventType: Deve retornar 'deposit'")
  void getEventTypeShouldReturnDeposit() {
    assertEquals("deposit", depositStrategy.getEventType());
  }

  @Test
  @DisplayName("execute: Deve chamar repositório e retornar resposta formatada")
  void executeShouldDepositAndReturnResponse() {
    // Arrange
    String accountId = "100";
    int amount = 20;
    EventRequest request = new EventRequest("deposit", null, accountId, amount);
    Account accountCreated = new Account(accountId, amount);

    // Mock do comportamento do repository
    when(repository.deposit(accountId, amount)).thenReturn(accountCreated);

    // Act
    EventResponse response = depositStrategy.execute(request);

    // Assert
    assertNotNull(response);
    assertEquals(accountId, response.destination().id());
    assertEquals(amount, response.destination().balance());

    // Verifica se a interação com o repositório foi exata
    verify(repository, times(1)).deposit(accountId, amount);
  }
}