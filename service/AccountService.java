package com.banking.service;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.banking.entity.Account;
import com.banking.entity.AccountType;
import com.banking.entity.Transaction;
import com.banking.entity.User;
import com.banking.exception.ResourceNotFoundException;
import com.banking.repository.AccountRepository;
import com.banking.repository.TransactionRepository;

@Service
public class AccountService {
	
	@Autowired
    private AccountRepository accountRepository;
	
	public Account createAccountForUser(User user) {

        Account account = new Account();
        account.setUser(user);
        
        // Static IFSC code
        account.setIfscCode("OBAP0000123");

        // Generate 11 digit account number
        String accNumber = generateAccountNumber();
        account.setAccountNumber(accNumber);
        
        int serialNum = Integer.parseInt(accNumber.substring(5));
        account.setSerialNumber(serialNum);
        
        account.setBalance(BigDecimal.ZERO);
        account.setAccountType(AccountType.SAVINGS);

        return accountRepository.save(account);
    }
	
	
    private String generateAccountNumber() {
    	String prefix = "19019"; // first 5 digits fixed

        Integer lastSerial = accountRepository.findMaxSerialNumber();
        if (lastSerial == null) {
            lastSerial = 0;
        }

        int newSerial = lastSerial + 1;

        // Last 6 digits padded
        String serialStr = String.format("%06d", newSerial);
        return prefix + serialStr; 
    	
	}


	private final TransactionRepository transactionRepository;

    public AccountService(AccountRepository accountRepository, TransactionRepository transactionRepository) {
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
    }

    public Account getByAccountNumber(String accountNumber) {
        return accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found"));
    }

    @Transactional
    public Transaction deposit(String accountNumber, BigDecimal amount, String description) {
        Account account = getByAccountNumber(accountNumber);
        account.setBalance(account.getBalance().add(amount));
        accountRepository.save(account);

        Transaction tx = new Transaction();
        // null during set from account
        tx.setFromAccount(null);
        tx.setToAccount(account);
        tx.setAmount(amount);
        tx.setType("DEPOSIT");
        return transactionRepository.save(tx);
    }

    @Transactional
    public Transaction withdraw(String accountNumber, BigDecimal amount) {
        Account account = getByAccountNumber(accountNumber);
        if (account.getBalance().compareTo(amount) < 0) {
            throw new IllegalArgumentException("Insufficient funds");
        }

        account.setBalance(account.getBalance().subtract(amount));
        accountRepository.save(account);

        
        Transaction tx = new Transaction();
        tx.setToAccount(account);
        tx.setAmount(amount);
        tx.setType("WITHDRAW");
        return transactionRepository.save(tx);
    }

    
    @Transactional
    public Transaction transfer(String fromAccount, String toAccount, BigDecimal amount) {
        withdraw(fromAccount, amount);
        return deposit(toAccount, amount, "Transfer from " + fromAccount);
    }
}
