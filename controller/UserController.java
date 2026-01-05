package com.banking.controller;

import com.banking.entity.Account;
import com.banking.entity.User;
import com.banking.repository.UserRepository;
import com.banking.service.AccountService;
import com.banking.service.EmailService;
import com.banking.service.OtpService;
import com.banking.service.UserService;
import jakarta.servlet.http.HttpSession;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class UserController {

	@Autowired
	private UserService userService;
	
	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private AccountService accountService;


	@Autowired
	private EmailService emailService;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	OtpService otpService;

	@GetMapping("/")
	public String home() {
		return "index";
	}

	@GetMapping("/register")
	public String showRegisterPage(Model model) {
		model.addAttribute("user", new User());
		return "register";
	}


	@GetMapping("/login")
	public String showLoginPage(@RequestParam(value = "error", required = false) String error, Model model) {
		if (error != null) {
			model.addAttribute("error", "Invalid email or password");
		}
		return "login";
	}

	@GetMapping("/reset-password")
	public String showResetPasswordPage() {
		return "reset-password";
	}

	@GetMapping("/verify-reset-otp")
	public String showVerifyResetOtpPage() {
		return "verify-reset-otp";
	}

	@GetMapping("/send-otp")
	@ResponseBody
	public String sendOtp(@RequestParam String email) {
		String otp = otpService.generateOTP(email);
		emailService.sendPasswordResetOtpEmail(email, otp);
		return "OTP sent to your mail!";
	}
	
	
	
	@PostMapping("/register")
	public String registerUser(@ModelAttribute("user") User user,
	                           @RequestParam("otp") String enteredOtp,
	                           @RequestParam("confirmPassword") String confirmPassword,
	                           HttpSession session,
	                           Model model) {

	    String sessionOtp = (String) session.getAttribute("regOtp");

	    if(sessionOtp == null || !enteredOtp.equals(sessionOtp)) {
	        model.addAttribute("error", "Invalid OTP");
	        model.addAttribute("user", user);   // add this

	        return "register";
	    }

	    // Password Validation
	    String passwordRegEx = "^(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&]).{8,}$";
	    if (!user.getPassword().matches(passwordRegEx)) {
	        model.addAttribute("error", "Password must have at least 8 characters, 1 uppercase letter, 1 number & 1 special character");
	        model.addAttribute("user", user); 
	        return "register";
	    }

	    if (!user.getPassword().equals(confirmPassword)) {
	        model.addAttribute("error", "Password & Confirm Password do not match");
	        model.addAttribute("user", user); 

	        return "register";
	    }

	    user.setPassword(passwordEncoder.encode(user.getPassword()));
	    User savedUser = userService.registerUser(user);
	  
	    accountService.createAccountForUser(savedUser);
	    session.removeAttribute("regOtp");

	    model.addAttribute("message", "Registration successful. Please Login.");
	    

	    
	    return "login";
	}

	@PostMapping("/update-profile")
	public String updateProfile(@RequestParam String password,
	                            @RequestParam String email,
	                            @RequestParam String phone,
	                            @RequestParam String address,
	                            HttpSession session,
	                            Model model) {

	    User loggedUser = (User) session.getAttribute("loggedInUser");

	    if (loggedUser == null) {
	        model.addAttribute("error", "Please Login First");
	        return "login";
	    }

	    String currentUserEmail = loggedUser.getEmail();

	    userService.updateProfile(currentUserEmail, password, email, phone, address);

	    return "redirect:/settings?success=true";
	}
	
	@GetMapping("/settings")
	public String settingsPage(Model model,Principal principal) {
	    String email = principal.getName();
	    User user = userService.findByEmail(email);
	    Account account = user.getAccount();
	    model.addAttribute("user", user);
	    model.addAttribute("account", account);

	    model.addAttribute("theme", user.getTheme());
	    model.addAttribute("language", user.getLanguage());
	    return "settings";
	}
	
	@PostMapping("/user/preferences")
	@ResponseBody
	public ResponseEntity<?> savePreferences(
	        @RequestParam String theme,
	        @RequestParam String language,
	        HttpSession session) {

	    User user = (User) session.getAttribute("loggedInUser");

	    if (user == null) {
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
	    }

	    user.setTheme(theme);
	    user.setLanguage(language);
	    userRepo.save(user);

	    session.setAttribute("loggedInUser", user); // refresh session

	    return ResponseEntity.ok().build();
	}


}
