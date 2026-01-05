package com.banking.repository;


import com.banking.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;


public interface AccountRepository extends JpaRepository<Account, Long> {
Optional<Account> findByAccountNumber(String accountNumber);

@Query("SELECT MAX(a.serialNumber) FROM Account a")
Integer findMaxSerialNumber();

}
