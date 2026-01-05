package com.banking.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.banking.entity.PaymentHistory;
import com.banking.entity.PaymentStatus;

public interface PaymentHistoryRepository
extends JpaRepository<PaymentHistory, Long> {

@Query("""
SELECT p FROM PaymentHistory p
WHERE p.userId = :userId
AND p.creditAccountId = :creditAccountId
AND p.status = 'PENDING' 
ORDER BY p.dueDate ASC
""")
PaymentHistory findLatestPending(
@Param("userId") Long userId,
@Param("creditAccountId") Long creditAccountId);

List<PaymentHistory> findByUserId(Long userId);
boolean existsByCreditAccountIdAndStatus(
        Long creditAccountId,
        PaymentStatus status);

}

