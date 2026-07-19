package com.ebanx.ledger.domain;

import com.ebanx.ledger.adapters.inbound.dto.request.EventRequest;
import com.ebanx.ledger.adapters.inbound.dto.response.EventResponse;
import com.ebanx.ledger.adapters.inbound.exception.AccountNotFoundException;
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
class WithdrawStrategyTest {

  @Mock
  private InMemoryAccountRepository repository;

  @InjectMocks
  private WithdrawStrategy withdrawStrategy;

  @Test
  @DisplayName("getEventType: Deve retornar 'withdraw'")
  void getEventTypeShouldReturnWithdraw() {
    assertEquals("withdraw", withdrawStrategy.getEventType());
  }

  @Test
  @DisplayName("execute: Deve processar saque com sucesso")
  void executeShouldWithdrawAndReturnResponse() {
    // Arrange
    EventRequest request = new EventRequest("withdraw", "100", null, 5);
    Account updatedAccount = new Account("100", 5);

    when(repository.withdraw("100", 5)).thenReturn(updatedAccount);

    // Act
    EventResponse response = withdrawStrategy.execute(request);

    // Assert
    assertNotNull(response);
    assertEquals("100", response.origin().id());
    assertEquals(5, response.origin().balance());
    verify(repository, times(1)).withdraw("100", 5);
  }

  @Test
  @DisplayName("execute: Deve lançar AccountNotFoundException quando repositório retornar null")
  void executeShouldThrowExceptionWhenRepositoryReturnsNull() {
    // Arrange
    EventRequest request = new EventRequest("withdraw", "999", null, 5);
    when(repository.withdraw("999", 5)).thenReturn(null);

    // Act & Assert
    assertThrows(AccountNotFoundException.class, () -> withdrawStrategy.execute(request));
    verify(repository, times(1)).withdraw("999", 5);
  }
}