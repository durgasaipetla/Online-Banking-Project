package com.banking.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.banking.entity.CreditAccount;
import com.banking.entity.PaymentHistory;
import com.banking.entity.PaymentStatus;
import com.banking.repository.CreditAccountRepository;
import com.banking.repository.PaymentHistoryRepository;
@Service
public class DueDateService {

    @Autowired
    private CreditAccountRepository creditRepo;

    @Autowired
    private PaymentHistoryRepository paymentRepo;

    @Scheduled(cron = "0 0 1 1 * ?") // 1st day of every month
    public void generateMonthlyDueDates() {

        List<CreditAccount> accounts =
                creditRepo.findByStatus("ACTIVE");

        for (CreditAccount acc : accounts) {

            boolean alreadyPending =
                paymentRepo.existsByCreditAccountIdAndStatus(
                    acc.getId(), PaymentStatus.PENDING);

            if (alreadyPending) continue; // 🔥 PREVENT DUPLICATES

            LocalDate today = LocalDate.now();
            LocalDate dueDate = today.withDayOfMonth(acc.getBillingDay());

            if (!dueDate.isAfter(today)) {
                dueDate = dueDate.plusMonths(1);
            }

            PaymentHistory p = new PaymentHistory();
            p.setUserId(acc.getUserId());
            p.setCreditAccountId(acc.getId());
            p.setDueDate(dueDate);
            p.setStatus(PaymentStatus.PENDING);

            paymentRepo.save(p);
        }
    }
}

