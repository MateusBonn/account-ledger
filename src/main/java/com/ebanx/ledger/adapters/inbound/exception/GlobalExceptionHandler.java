package com.ebanx.ledger.adapters.inbound.exception;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(AccountNotFoundException.class)
  public ResponseEntity<Integer> handleAccountNotFound() {
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(0);
  }

  @ExceptionHandler({IllegalStateException.class, IllegalArgumentException.class})
  public ResponseEntity<Integer> handleBusinessRuleViolation() {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(0);
  }
}
