package com.banking.scheduler;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.banking.entity.Emi;
import com.banking.entity.PaymentHistory;
import com.banking.entity.PaymentStatus;
import com.banking.repository.EmiRepository;
import com.banking.repository.PaymentHistoryRepository;
import com.banking.service.FicoService;

@Component
@EnableScheduling
public class EmiScheduler {

    @Autowired
    private EmiRepository emiRepo;

    @Autowired
    private FicoService ficoService;

    @Scheduled(cron = "0 0 1 * * ?") // Daily 1 AM
    public void markOverdueEmis() {

        LocalDate today = LocalDate.now();

        List<Emi> emis = emiRepo.findAll();

        for (Emi emi : emis) {
            if ("PENDING".equals(emi.getStatus()) &&
                today.isAfter(emi.getDueDate().plusDays(3))) {

                emi.setStatus("OVERDUE");
                emiRepo.save(emi);

                ficoService.updateFico(
                    emi.getLoan().getUser(),
                    false
                );
            }
        }
    }
}


