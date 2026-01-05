package com.banking.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.banking.entity.Emi;
import com.banking.entity.Loan;

public interface EmiRepository extends JpaRepository<Emi, Long> {
    List<Emi> findByLoan(Loan loan);
}

