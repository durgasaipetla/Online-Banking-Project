package com.banking.controller;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.banking.entity.Emi;
import com.banking.entity.Loan;
import com.banking.repository.EmiRepository;
import com.banking.repository.LoanRepository;
import com.banking.service.FicoService;
import com.banking.service.RazorpayService;
import com.razorpay.Order;

import jakarta.transaction.Transactional;


@RestController
@RequestMapping("/loan")
public class EmiController {
	  @Autowired
	    private EmiRepository emiRepo;
	  
	  @Autowired
	    private RazorpayService razorpayService;

	  @Autowired
	    private  LoanRepository loanRepo;
	  
	  @Autowired
	    private FicoService ficoService;

	

	  @PostMapping("/emi/payment-success")
	  @Transactional
	  public ResponseEntity<?> emiSuccess(@RequestParam Long emiId,
	                                      @RequestParam String paymentId) {

	      Emi emi = emiRepo.findById(emiId)
	              .orElseThrow(() -> new RuntimeException("EMI not found"));

	      // 🔒 BLOCK EARLY PAYMENT (IMPORTANT)
	      if (emi.getDueDate().isAfter(java.time.LocalDate.now())) {
	          return ResponseEntity
	                  .badRequest()
	                  .body("EMI cannot be paid before due date");
	      }

	      emi.setStatus("PAID");
	      emi.setPaidDate(LocalDateTime.now());
	      emi.setPaymentId(paymentId);
	      emiRepo.save(emi);

	      boolean allPaid = emiRepo.findByLoan(emi.getLoan())
	              .stream()
	              .allMatch(e -> "PAID".equals(e.getStatus()));

	      if (allPaid) {
	          Loan loan = emi.getLoan();
	          loan.setStatus("CLOSED");
	          loanRepo.save(loan);
	      }

	      ficoService.updateFico(emi.getLoan().getUser(), true);

	      return ResponseEntity.ok("EMI Paid Successfully");
	  }

    @GetMapping("/emi/{id}")
    public ResponseEntity<?> getEmi(@PathVariable Long id) {

        Emi emi = emiRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("EMI not found"));

        Map<String, Object> response = new HashMap<>();
        response.put("id", emi.getId());
        response.put("emiNumber", emi.getEmiNumber());
        response.put("emiAmount", emi.getEmiAmount());
        response.put("dueDate", emi.getDueDate());
        response.put("status", emi.getStatus());

        return ResponseEntity.ok(response);
    }



}
