package com.ebanx.ledger.adapters.outbound.repository;

import com.ebanx.ledger.adapters.inbound.exception.BalanceinsufficientException;
import com.ebanx.ledger.adapters.inbound.dto.response.TransferResponse;
import com.ebanx.ledger.domain.model.Account;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryAccountRepositoryTest {

  private InMemoryAccountRepository repository;

  @BeforeEach
  void setUp() {
    repository = new InMemoryAccountRepository();
  }

  @Test
  @DisplayName("deposit: Deve criar conta com saldo se não existir")
  void depositShouldCreateNewAccount() {
    Account result = repository.deposit("100", 20);

    assertEquals(20, result.balance());
    assertEquals("100", result.id());
  }

  @Test
  @DisplayName("deposit: Deve somar saldo se conta já existir")
  void depositShouldIncrementBalance() {
    repository.deposit("100", 20);
    Account result = repository.deposit("100", 10);

    assertEquals(30, result.balance());
  }

  @Test
  @DisplayName("withdraw: Deve subtrair saldo com sucesso")
  void withdrawShouldDecrementBalance() {
    repository.deposit("100", 20);
    Account result = repository.withdraw("100", 5);

    assertEquals(15, result.balance());
  }

  @Test
  @DisplayName("withdraw: Deve lançar BalanceinsufficientException se saldo insuficiente")
  void withdrawShouldThrowExceptionWhenBalanceIsLow() {
    repository.deposit("100", 5);

    assertThrows(BalanceinsufficientException.class, () -> repository.withdraw("100", 10));
  }

  @Test
  @DisplayName("withdraw: Deve retornar null se conta não existir")
  void withdrawShouldReturnNullWhenAccountNotFound() {
    assertNull(repository.withdraw("999", 10));
  }

  @Test
  @DisplayName("transfer: Deve transferir saldo entre duas contas com sucesso")
  void transferShouldMoveBalanceBetweenAccounts() {
    repository.deposit("1", 100);
    repository.deposit("2", 10);

    TransferResponse result = repository.transfer("1", "2", 50);

    assertEquals(50, result.origin().balance());
    assertEquals(60, result.destination().balance());
  }

  @Test
  @DisplayName("transfer: Deve lançar BalanceinsufficientException se origem sem saldo")
  void transferShouldThrowExceptionWhenOriginHasInsufficientFunds() {
    repository.deposit("1", 10);
    repository.deposit("2", 10);

    assertThrows(BalanceinsufficientException.class, () -> repository.transfer("1", "2", 50));
  }

  @Test
  @DisplayName("transfer: Deve retornar null se conta de origem não existir")
  void transferShouldReturnNullWhenOriginDoesNotExist() {
    assertNull(repository.transfer("999", "2", 10));
  }
}