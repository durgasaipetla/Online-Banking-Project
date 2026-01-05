package com.banking.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.banking.entity.Payment;
import com.banking.entity.User;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
	
    List<Payment> findByUser_Id(Long userId);

}





