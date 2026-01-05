package com.banking.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.banking.entity.FicoHistory;
import com.banking.entity.User;
import com.banking.repository.FicoHistoryRepository;

@Service
public class FicoService {

    @Autowired
    private FicoHistoryRepository ficoRepo;

    public int updateFico(User user, boolean onTimePayment) {

        int score = user.getFicoScore();

        if (onTimePayment) {
            score += 5;
        } else {
            score -= 20;
        }

        user.setFicoScore(score);

        FicoHistory history = new FicoHistory();
        history.setUser(user);
        history.setScore(score);
        history.setReason(onTimePayment ? "On-time EMI payment" : "Missed EMI");
        history.setCreatedAt(LocalDateTime.now());

        ficoRepo.save(history);
        return score;
    }
}
