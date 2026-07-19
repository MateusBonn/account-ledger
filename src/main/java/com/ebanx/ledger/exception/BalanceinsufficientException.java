package com.ebanx.ledger.exception;

public class BalanceinsufficientException extends RuntimeException {
  private final String accountId;

  public BalanceinsufficientException(String accountId) {
    super("Insufficient balance for account ID: " + accountId);
    this.accountId = accountId;
  }

  public String getAccountId() {
    return accountId;
  }
}
