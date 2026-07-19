package com.ebanx.ledger.repository;

import com.ebanx.ledger.dto.response.TransferResponse;
import com.ebanx.ledger.exception.BalanceinsufficientException;
import com.ebanx.ledger.model.Account;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
public class InMemoryAccountRepository {

  private final ConcurrentMap<String, Account> store = new ConcurrentHashMap<>();

  public Optional<Account> findById(String id) {
    return Optional.ofNullable(store.get(id));
  }

  public void clear() {
    store.clear();
  }

  public Account deposit(String id, int amount) {
    return store.compute(id, (key, currentAccount) -> {
      if (currentAccount == null) {
        return new Account(id, amount);
      }
      return new Account(id, currentAccount.balance() + amount);
    });
  }

  public Account withdraw(String id, int amount) {
    Account account = store.get(id);
    if (account == null) return null;
    if (account.balance() < amount) throw new BalanceinsufficientException(id);

    Account updated = new Account(id, account.balance() - amount);
    store.put(id, updated);
    return updated;
  }

  public synchronized TransferResponse transfer(String originId, String destinationId, int amount) {
    Account origin = store.get(originId);
    if (origin == null) return null;
    if (origin.balance() < amount) throw new BalanceinsufficientException(originId);

    Account updatedOrigin = new Account(originId, origin.balance() - amount);
    store.put(originId, updatedOrigin);

    Account destination = store.compute(destinationId, (key, value) ->
        value == null ? new Account(destinationId, amount) : new Account(destinationId, value.balance() + amount));

    return new TransferResponse(updatedOrigin, destination);
  }
}
