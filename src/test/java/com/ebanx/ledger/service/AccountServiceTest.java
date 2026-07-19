package com.ebanx.ledger.service;

import com.ebanx.ledger.dto.request.EventRequest;
import com.ebanx.ledger.dto.response.EventResponse;
import com.ebanx.ledger.exception.AccountNotFoundException;
import com.ebanx.ledger.repository.InMemoryAccountRepository;
import com.ebanx.ledger.model.Account;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

  @Mock
  private InMemoryAccountRepository repository;

  @Mock
  private EventStrategy eventStrategy;

  private AccountService accountService;

  @BeforeEach
  void setUp() {
    // Configuramos a lista de estratégias com o mock que criamos
    lenient().when(eventStrategy.getEventType()).thenReturn("deposit");
    accountService = new AccountService(List.of(eventStrategy), repository);
  }

  @Test
  @DisplayName("getBalance: Deve retornar saldo quando conta existir")
  void getBalanceShouldReturnBalanceWhenAccountExists() {
    String accountId = "1";
    Account account = new Account(accountId, 100);
    when(repository.findById(accountId)).thenReturn(Optional.of(account));

    Integer balance = accountService.getBalance(accountId);

    assertEquals(100, balance);
    verify(repository, times(1)).findById(accountId);
  }

  @Test
  @DisplayName("getBalance: Deve lançar AccountNotFoundException quando conta não existir")
  void getBalanceShouldThrowExceptionWhenAccountDoesNotExist() {
    String accountId = "999";
    when(repository.findById(accountId)).thenReturn(Optional.empty());

    assertThrows(AccountNotFoundException.class, () -> accountService.getBalance(accountId));
  }

  @Test
  @DisplayName("processEvent: Deve executar estratégia com sucesso")
  void processEventShouldExecuteStrategyWhenTypeIsValid() {
    EventRequest request = new EventRequest("deposit", null, "1", 100);
    EventResponse expectedResponse = new EventResponse(new Account("1", 100), null);

    when(eventStrategy.execute(request)).thenReturn(expectedResponse);

    EventResponse response = accountService.processEvent(request);

    assertNotNull(response);
    assertEquals(100, response.origin().balance());
    verify(eventStrategy, times(1)).execute(request);
  }

  @Test
  @DisplayName("processEvent: Deve lançar IllegalArgumentException quando tipo de evento for desconhecido")
  void processEventShouldThrowExceptionWhenStrategyNotFound() {
    EventRequest request = new EventRequest("invalid_type", null, "1", 100);

    assertThrows(IllegalArgumentException.class, () -> accountService.processEvent(request));

    verify(eventStrategy, never()).execute(request);
  }
}