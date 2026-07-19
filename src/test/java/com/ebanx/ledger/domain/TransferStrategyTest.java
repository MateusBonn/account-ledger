package com.ebanx.ledger.domain;

import com.ebanx.ledger.adapters.inbound.dto.request.EventRequest;
import com.ebanx.ledger.adapters.inbound.dto.response.EventResponse;
import com.ebanx.ledger.adapters.inbound.dto.response.TransferResponse;
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
class TransferStrategyTest {

  @Mock
  private InMemoryAccountRepository repository;

  @InjectMocks
  private TransferStrategy transferStrategy;

  @Test
  @DisplayName("getEventType: Deve retornar 'transfer'")
  void getEventTypeShouldReturnTransfer() {
    assertEquals("transfer", transferStrategy.getEventType());
  }

  @Test
  @DisplayName("execute: Deve processar transferência com sucesso")
  void executeShouldTransferAndReturnResponse() {
    // Arrange
    EventRequest request = new EventRequest("transfer", "1", "2", 10);
    Account origin = new Account("1", 90);
    Account destination = new Account("2", 10);
    TransferResponse transferResult = new TransferResponse(origin, destination);

    when(repository.transfer("1", "2", 10)).thenReturn(transferResult);

    // Act
    EventResponse response = transferStrategy.execute(request);

    // Assert
    assertNotNull(response);
    assertEquals(origin.id(), response.origin().id());
    assertEquals(destination.id(), response.destination().id());
    verify(repository, times(1)).transfer("1", "2", 10);
  }

  @Test
  @DisplayName("execute: Deve lançar AccountNotFoundException quando repositório retornar null")
  void executeShouldThrowExceptionWhenRepositoryReturnsNull() {
    // Arrange
    EventRequest request = new EventRequest("transfer", "999", "2", 10);
    when(repository.transfer("999", "2", 10)).thenReturn(null);

    // Act & Assert
    assertThrows(AccountNotFoundException.class, () -> transferStrategy.execute(request));
    verify(repository, times(1)).transfer("999", "2", 10);
  }
}