package com.banking.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.banking.entity.User;
import com.banking.service.EmailService;
import com.banking.service.OtpService;
import com.banking.service.UserService;

import jakarta.servlet.http.HttpSession;

@Controller
public class OtpController {
	 @Autowired
	    private UserService userService;

	    @Autowired
	    private EmailService emailService;

	    @Autowired
	    private PasswordEncoder passwordEncoder;

	    @Autowired
	    OtpService otpService;
	    
	    @PostMapping("/send-register-otp")
		@ResponseBody
		public String sendRegisterOtp(@RequestParam String email,HttpSession session) {

		    User user = userService.findByEmail(email);
		    if(user != null){
		        return "Email already exists!";
		    }

		    String otp = otpService.generateOTP(email);
		    emailService.sendRegistrationOtpEmail(email, otp);
		    session.setAttribute("regOtp", otp);


		    return "OTP Sent Successfully";
		    
		}

	    @PostMapping("/verify-registration-otp")
	    public String verifyRegistrationOtp(@RequestParam int otp, HttpSession session, Model model) {

	        int sessionOtp = (int) session.getAttribute("otp");
	        User user = (User) session.getAttribute("tempUser");

	        if (otp == sessionOtp) {

	            user.setPassword(passwordEncoder.encode(user.getPassword()));
	            userService.registerUser(user);

	            session.removeAttribute("otp");
	            session.removeAttribute("tempUser");

	            model.addAttribute("message", "Registration Successful!");
	            return "login";

	        } else {
	            model.addAttribute("error", "Invalid OTP. Try again.");
	            return "verify-reset-otp";
	        }
	    }

		    
		 // STEP 1 : user enters email -> send OTP
		@PostMapping("/send-reset-otp")
	    public String sendResetOtp(@RequestParam String email, Model model, HttpSession session){
	        User user = userService.findByEmail(email);
	
	        if(user != null){
	            String otp = otpService.generateOTP(email);
	            emailService.sendPasswordResetOtpEmail(email, otp);
	            session.setAttribute("resetEmail", email);
	            model.addAttribute("message", "Reset OTP sent to your email.");
	            return "verify-reset-otp";
	        } else {
	            model.addAttribute("error","No user found with this email");
	            return "reset-password";
	        }
	    }
		

	    // STEP 2 : verify OTP + update password
	    @PostMapping("/verify-reset-otp")
	    public String verifyResetOtp(@RequestParam String otp,
	                                 @RequestParam String newPassword,
	                                 HttpSession session,
	                                 Model model) {
	        String email = (String) session.getAttribute("resetEmail");
	
	        if(email == null){
	            model.addAttribute("error", "Session expired or email not found");
	            return "reset-password";
	        }
	
	        boolean validOtp = otpService.validateOtp(email, otp);
	
	        if(validOtp){
	            userService.updatePassword(email, passwordEncoder.encode(newPassword));
	            model.addAttribute("message", "Password updated successfully! Please Login");
	            session.removeAttribute("resetEmail");
	            return "login";
	        } else {
	            model.addAttribute("error", "Invalid OTP! Try again");
	            return "verify-reset-otp";
	        }
	    }

}
