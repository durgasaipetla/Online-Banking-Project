package com.banking.entity;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "credit_accounts")
public class CreditAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private String creditType;   // CREDIT_CARD, LOAN

    @Column(nullable = false)
    private int billingDay;      // ✅ REQUIRED (5, 10, 15…)

    @Column(nullable = false)
    private LocalDate startDate;

    @Column(nullable = false)
    private String status;       // ACTIVE, CLOSED

    // 🔹 REQUIRED by JPA
    public CreditAccount() {}

    // 🔹 Getters & setters
    public int getBillingDay() {
        return billingDay;
    }

    public void setBillingDay(int billingDay) {
        this.billingDay = billingDay;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getId() {
        return id;
    }
}
