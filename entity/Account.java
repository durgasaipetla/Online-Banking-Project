package com.banking.entity;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "accounts")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
 
    @Column(nullable = false, unique = true)
    private String accountNumber;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false)
    private BigDecimal balance = BigDecimal.ZERO;  // correct for money

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AccountType accountType = AccountType.SAVINGS;

    @Column(nullable = false)
    private boolean active = true;

    @Column(nullable = false)
    private String ifscCode;

    @Column(nullable = false)
    private LocalDate createdDate = LocalDate.now(); // default creation date

    @Column(name ="serial_number", unique =true)
    private Integer serialNumber;
    
    
  
    // Getters & Setters
    public Long getId() { 
    	return id; 
    	}
    public void setId(Long id) { 
    	this.id = id; 
    	}

    public String getAccountNumber() {
    	return accountNumber;
    	}
    public void setAccountNumber(String accountNumber) { 
    	this.accountNumber = accountNumber;
    	}

    public User getUse() {
    	return user ; 
    	}
    public void setUser(User user) {
    	this.user = user;
    	}

    public BigDecimal getBalance() {
    	return balance;
    	}
    public void setBalance(BigDecimal balance) {
    	this.balance = balance;
    	}

    public AccountType getAccountType() {
    	return accountType;
    	}
    public void setAccountType(AccountType accountType) {
    	this.accountType = accountType;
    	}

    public boolean isActive() { 
    	return active; 
    	}
    public void setActive(boolean active) { 
    	this.active = active;
    	}

    public String getIfscCode() {
    	return ifscCode;
    	}
    public void setIfscCode(String ifscCode) { 
    	this.ifscCode = ifscCode;
    	}

    public LocalDate getCreatedDate() {
    	return createdDate; 
    	}
    public void setCreatedDate(LocalDate createdDate) {
    	this.createdDate = createdDate; 
    	}
    public Integer getSerialNumber() {
    	return serialNumber;
    }
    public void setSerialNumber(Integer serialNumber) {
        this.serialNumber = serialNumber;
    }
}
