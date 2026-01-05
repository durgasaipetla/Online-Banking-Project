package com.banking.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.banking.entity.PaymentHistory;
import com.banking.entity.PaymentStatus;
import com.banking.entity.User;
import com.banking.repository.PaymentHistoryRepository;
import com.banking.repository.UserRepository;
import com.banking.service.CibilScoreService;

import jakarta.servlet.http.HttpSession;

@Controller
public class CibilController extends BaseController {

    @Autowired
    private PaymentHistoryRepository paymentRepo;

    @Autowired
    private CibilScoreService scoreService;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/fico-score")
    public String ficoScore(Model model, HttpSession session) {

    	if (!isLoggedIn(session)) 
    		return "redirect:/login";
        
        User user = (User) session.getAttribute("loggedInUser");

        List<PaymentHistory> payments =
                paymentRepo.findByUserId(user.getId());

        int score = scoreService.calculateScore(payments);
        String status = scoreService.getStatus(score);

        user.setFicoScore(score);
        userRepository.save(user);
        session.setAttribute("loggedInUser", user);
       
        long onTime = payments.stream()
                .filter(p -> p.getStatus() == PaymentStatus.PAID)
                .count();

        model.addAttribute("ficoScore", score);
        model.addAttribute("ficoStatus", status);
        model.addAttribute("onTime", onTime);
        model.addAttribute("total", payments.size());

        return "fico-score";
    }
}
