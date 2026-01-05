package com.banking.controller;

import com.banking.entity.Payment;
import com.banking.entity.User;
import com.banking.repository.PaymentRepository;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class PaymentHistoryController extends BaseController{

    @Autowired
    private PaymentRepository paymentRepository;

    @GetMapping("/payment-history")
    public String viewPaymentHistory(HttpSession session, Model model) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
       
        if (!isLoggedIn(session)) return 
        		"redirect:/login";


        // ✅ Fetch only this user's payments
        List<Payment> userPayments = paymentRepository.findByUser_Id(loggedInUser.getId());
        model.addAttribute("payments", userPayments);

        return "payment-history";
    }
}
