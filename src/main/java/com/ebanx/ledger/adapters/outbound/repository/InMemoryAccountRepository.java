package com.ebanx.ledger.adapters.outbound.repository;

import com.ebanx.ledger.adapters.inbound.dto.response.TransferResponse;
import com.ebanx.ledger.domain.model.Account;
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

  public void save(Account account) {
    store.put(account.id(), account);
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
    Account acc = store.get(id);
    if (acc == null) return null;
    if (acc.balance() < amount) throw new IllegalStateException("Saldo insuficiente");

    Account updated = new Account(id, acc.balance() - amount);
    store.put(id, updated);
    return updated;
  }

  public synchronized TransferResponse transfer(String originId, String destinationId, int amount) {
    Account origin = store.get(originId);
    if (origin == null) return null;
    if (origin.balance() < amount) throw new IllegalStateException("Saldo insuficiente");

    Account updatedOrigin = new Account(originId, origin.balance() - amount);
    store.put(originId, updatedOrigin);

    Account dest = store.compute(destinationId, (k, v) ->
        v == null ? new Account(destinationId, amount) : new Account(destinationId, v.balance() + amount));

    return new TransferResponse(updatedOrigin, dest);
  }
}
