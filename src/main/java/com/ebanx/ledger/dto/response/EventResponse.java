package com.ebanx.ledger.dto.response;

import com.ebanx.ledger.model.Account;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record EventResponse(Account origin, Account destination) {

  public static EventResponse fromDeposit(Account destination) {
    return new EventResponse(null, destination);
  }

  public static EventResponse fromWithdraw(Account origin) {
    return new EventResponse(origin, null);
  }

  public static EventResponse fromTransfer(Account origin, Account destination) {
    return new EventResponse(origin, destination);
  }
}
