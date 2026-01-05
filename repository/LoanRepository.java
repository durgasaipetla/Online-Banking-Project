package com.banking.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.banking.entity.Emi;
import com.banking.entity.FicoHistory;
import com.banking.entity.Loan;
import com.banking.entity.User;

public interface LoanRepository extends JpaRepository<Loan, Long> {
    List<Loan> findByUser(User user);
    Optional<Loan> findByUserAndStatus(User user, String status);

}
