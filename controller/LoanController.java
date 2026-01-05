package com.banking.controller;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.banking.entity.Emi;
import com.banking.entity.Loan;
import com.banking.entity.User;
import com.banking.repository.EmiRepository;
import com.banking.repository.LoanRepository;
import com.banking.service.FicoService;
import com.banking.service.LoanService;
import com.banking.service.RazorpayService;
import com.razorpay.Order;

import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;

@RestController
@RequestMapping("/loan")
public class LoanController {

  
    
  
    @Autowired
    private LoanService loanService;

    
   
    @PostMapping("/apply")
    public ResponseEntity<?> applyLoan(@RequestParam double amount,
                                       @RequestParam int tenure,
                                       @RequestParam String type,
                                       HttpSession session) {

        User user = (User) session.getAttribute("loggedInUser");

        if (user == null)
            return ResponseEntity.status(401).body("User not logged in");

        if (user.getFicoScore() < 580)
            return ResponseEntity.badRequest().body("Loan rejected due to low FICO");
		try {
			 Loan loan = loanService.createLoan(user, type, amount, tenure, user.getFicoScore());
		     return ResponseEntity.ok("Loan Approved. EMI Amount: " + loan.getEmiAmount());	
		}
		catch(IllegalStateException e){
		    return ResponseEntity.badRequest().body("You already have an active loan");
		}
        
       
    }



    @GetMapping("/active")
    public ResponseEntity<?> getActiveLoan(HttpSession session) {

        User user = (User) session.getAttribute("loggedInUser");
        if (user == null)
            return ResponseEntity.status(401).build();

        return ResponseEntity.ok(
            loanService.getActiveLoanWithEmis(user.getId())
        );
    }

}
