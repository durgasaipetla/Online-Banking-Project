package com.banking.controller;


import com.banking.entity.Account;
import com.banking.entity.Transaction;
import com.banking.service.AccountService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.math.BigDecimal;


@RestController
@RequestMapping("/api/accounts")
public class AccountController {
private final AccountService accountService;


public AccountController(AccountService accountService) {
this.accountService = accountService;
}


@GetMapping("/{accountNumber}")
public ResponseEntity<Account> getAccount(@PathVariable String accountNumber) {
Account a = accountService.getByAccountNumber(accountNumber);
return ResponseEntity.ok(a);
}


@PostMapping("/{accountNumber}/deposit")
public ResponseEntity<Transaction> deposit(@PathVariable String accountNumber, @RequestParam String amount, @RequestParam(required = false) String desc) {
BigDecimal amt = new BigDecimal(amount);
Transaction tx = accountService.deposit(accountNumber, amt, desc);
return ResponseEntity.ok(tx);
}


@PostMapping("/{accountNumber}/withdraw")
public ResponseEntity<Transaction> withdraw(@PathVariable String accountNumber, @RequestParam String amount, @RequestParam(required = false) String desc) {
BigDecimal amt = new BigDecimal(amount);
Transaction tx = accountService.withdraw(accountNumber, amt);
return ResponseEntity.ok(tx);
}


@PostMapping("/transfer")
public ResponseEntity<Transaction> transfer(@RequestParam String from, @RequestParam String to, @RequestParam String amount, @RequestParam(required = false) String desc) {
BigDecimal amt = new BigDecimal(amount);
Transaction tx = accountService.transfer(from, to, amt);
return ResponseEntity.ok(tx);
}
}