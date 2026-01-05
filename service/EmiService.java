package com.banking.service;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.banking.entity.Emi;
import com.banking.entity.Loan;
import com.banking.repository.EmiRepository;

@Service
public class EmiService {
    @Autowired
    private EmiRepository emiRepo;
    
    public void generateEmis(Loan loan) {

        for (int i = 1; i <= loan.getTenureMonths(); i++) {

            Emi emi = new Emi();
            emi.setLoan(loan);
            emi.setEmiNumber(i);
            emi.setEmiAmount(loan.getEmiAmount());
            emi.setDueDate(LocalDate.now().plusMonths(i));
            emi.setStatus("PENDING");
            emiRepo.save(emi);
       
        }
    }
}
