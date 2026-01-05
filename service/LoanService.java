package com.banking.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.banking.dto.ActiveLoanDto;
import com.banking.entity.Emi;
import com.banking.entity.Loan;
import com.banking.entity.User;
import com.banking.repository.EmiRepository;
import com.banking.repository.LoanRepository;

import jakarta.transaction.Transactional;

@Service
public class LoanService {
	
	@Autowired private EmiRepository emiRepo;
	
    @Autowired LoanRepository loanRepo;
    
    @Autowired EmiService emiService;

    @Transactional
    public Loan createLoan(User user, String type, double amount, int tenure, int fico) {

    	  if (loanRepo.findByUserAndStatus(user, "ACTIVE").isPresent()) {
    	        throw new IllegalStateException("ACTIVE_LOAN_EXISTS");
    	    }
    	  
    	double rate = fico >= 750 ? 10 : fico >= 670 ? 12 : 15;
        double emi = calculateEmi(amount, rate, tenure);
        double total = emi * tenure;

        Loan loan = new Loan();
        loan.setUser(user);
        loan.setLoanType(type);
        loan.setPrincipalAmount(amount);
        loan.setInterestRate(rate);
        loan.setTenureMonths(tenure);
        loan.setEmiAmount(emi);
        loan.setTotalAmount(total);
        loan.setStatus("ACTIVE");
        loan.setCreatedAt(LocalDateTime.now());

        Loan savedLoan = loanRepo.saveAndFlush(loan);
        emiService.generateEmis(savedLoan);

        return savedLoan;
    }
    public double calculateEmi(double principal, double annualRate, int months) {
        double r = annualRate / 12 / 100;
        return (principal * r * Math.pow(1+r, months)) /
               (Math.pow(1+r, months) - 1);
    }
   
    public ActiveLoanDto getActiveLoanWithEmis(Long userId) {

        // Create lightweight User reference (NO DB hit)
        User user = new User();
        user.setId(userId);

        Loan loan = loanRepo.findByUserAndStatus(user, "ACTIVE")
                .orElse(null);

        if (loan == null) return null;

        List<Emi> emis = emiRepo.findByLoan(loan);

        return new ActiveLoanDto(loan, emis);
    }

}
