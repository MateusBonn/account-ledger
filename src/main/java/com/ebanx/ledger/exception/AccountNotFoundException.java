package com.ebanx.ledger.exception;

public class AccountNotFoundException extends RuntimeException {

  private final String accountId;

  public AccountNotFoundException(String accountId) {
    super("Account not found for ID: " + accountId);
    this.accountId = accountId;
  }

  public String getAccountId() {
    return accountId;
  }
}
