package com.banking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.banking.entity.Investment;

public interface InvestmentRepository extends JpaRepository<Investment, Long> {
}
