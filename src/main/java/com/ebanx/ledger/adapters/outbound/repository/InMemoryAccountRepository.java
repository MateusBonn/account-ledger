package com.ebanx.ledger.adapters.outbound.repository;

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
}
