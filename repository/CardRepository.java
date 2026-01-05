package com.banking.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.banking.entity.Card;

public interface CardRepository extends JpaRepository<Card, Long> {

	Card findByEmail(String email);}
