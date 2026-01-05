package com.banking.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.HttpServletResponse;


@Controller
public class HomeController extends BaseController {
	
	/*
	 * @GetMapping("/fico-score") public String ficoScore(HttpSession session) {
	 * 
	 * if (!isLoggedIn(session)) return "redirect:/login"; return "fico-score"; }
	 */

	    @GetMapping("/payments")
	    public String payments(HttpSession session) {
	      
	        if (!isLoggedIn(session)) return "redirect:/login";
	        return "razorpay/page"; // your Razorpay page
	    }

	    
	    @GetMapping("/invest-banking")
	    public String investBanking(HttpSession session) {
	    	if (!isLoggedIn(session)) return "redirect:/login";
	    	return "invest-banking";
	    }
	    
	    @GetMapping("/investment")
	    public String showInvestmentPage(HttpSession session) {
	    	
	    	if (!isLoggedIn(session)) return "redirect:/login";
	    	return "investment";
	    }
	    @GetMapping("/referral")
	    public String showReferalPage(HttpSession session) {
	    	
	    	if (!isLoggedIn(session)) return "redirect:/login";
	    	return "referral";
	    }
	    @GetMapping("/weekly-spending")
	    public String showWeeklySpendingPage(HttpSession session) {
	    	
	    	if (!isLoggedIn(session)) return "redirect:/login";
	    	return "weeklyspending";
	    }
	    @PostMapping("/logout")
	    public String logout(HttpSession session) {
	        session.invalidate();
	        return "redirect:/login";
	    }


}
