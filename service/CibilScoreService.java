package com.banking.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.banking.entity.PaymentHistory;
import com.banking.entity.PaymentStatus;

@Service
public class CibilScoreService {

    public int calculateScore(List<PaymentHistory> payments) {

        if (payments.isEmpty()) return 650;

        int score = 900;

        long onTime = payments.stream()
                .filter(p -> p.getStatus() == PaymentStatus.PAID)
                .count();

        long late = payments.stream()
                .filter(p -> p.getStatus() == PaymentStatus.LATE)
                .count();

        long missed = payments.stream()
                .filter(p -> p.getStatus() == PaymentStatus.MISSED)
                .count();

        // Heavy penalties
        score -= late * 15;
        score -= missed * 40;

        // Reward consistency
        double ratio = (double) onTime / payments.size();
        score += (int)(ratio * 40);

        return Math.max(300, Math.min(900, score));
    }

    public String getStatus(int score) {
        if (score < 580) return "Poor";
        if (score < 670) return "Fair";
        if (score < 740) return "Good";
        if (score < 800) return "Very Good";
        return "Excellent";
    }
}

