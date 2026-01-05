package com.banking.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.banking.entity.CreditAccount;

public interface CreditAccountRepository
extends JpaRepository<CreditAccount, Long> {

List<CreditAccount> findByStatus(String status);
}
