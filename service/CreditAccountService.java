package com.banking.service;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.banking.entity.PaymentHistory;
import com.banking.entity.PaymentStatus;
import com.banking.repository.PaymentHistoryRepository;


@Service
public class CreditAccountService {

    @Autowired
    private PaymentHistoryRepository paymentHistoryRepository;

    public void generateFirstDue(
            Long userId,
            Long creditAccountId,
            int billingDay) {

        LocalDate today = LocalDate.now();

        LocalDate dueDate = today.withDayOfMonth(billingDay);

        if (!dueDate.isAfter(today)) {
            dueDate = dueDate.plusMonths(1);
        }

        PaymentHistory ph = new PaymentHistory();
        ph.setUserId(userId);
        ph.setCreditAccountId(creditAccountId);
        ph.setDueDate(dueDate);
        ph.setStatus(PaymentStatus.PENDING);

        paymentHistoryRepository.save(ph);
    }
}
