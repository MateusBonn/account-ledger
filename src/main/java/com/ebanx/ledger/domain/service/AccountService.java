package com.ebanx.ledger.domain.service;

import com.ebanx.ledger.adapters.inbound.dto.request.EventRequest;
import com.ebanx.ledger.adapters.inbound.dto.response.DepositResponse;
import com.ebanx.ledger.adapters.inbound.dto.response.EventResponse;
import com.ebanx.ledger.adapters.inbound.dto.response.TransferResponse;
import com.ebanx.ledger.adapters.inbound.dto.response.WithdrawResponse;
import com.ebanx.ledger.adapters.inbound.exception.AccountNotFoundException;
import com.ebanx.ledger.adapters.outbound.repository.InMemoryAccountRepository;
import com.ebanx.ledger.domain.model.Account;
import org.springframework.stereotype.Service;

@Service
public class AccountService {

  private final InMemoryAccountRepository repository;

  public AccountService(InMemoryAccountRepository repository) {
    this.repository = repository;
  }

  public EventResponse processEvent(EventRequest request) {
    return switch (request.type()) {
      case "deposit" -> {
        Account dest = repository.deposit(request.destination(), request.amount());
        yield EventResponse.fromDeposit(dest);
      }
      case "withdraw" -> {
        Account origin = repository.withdraw(request.origin(), request.amount());
        if (origin == null) throw new AccountNotFoundException(request.origin());
        yield EventResponse.fromWithdraw(origin);
      }
      case "transfer" -> {
        var res = repository.transfer(request.origin(), request.destination(), request.amount());
        if (res == null) throw new AccountNotFoundException(request.origin());
        yield EventResponse.fromTransfer(res.origin(), res.destination());
      }
      default -> throw new IllegalArgumentException("Unknown event type");
    };
  }

}
