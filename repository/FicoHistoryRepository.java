package com.banking.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.banking.entity.FicoHistory;

public interface FicoHistoryRepository extends JpaRepository<FicoHistory, Long> {
}