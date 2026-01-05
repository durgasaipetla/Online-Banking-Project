package com.banking.controller;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.banking.entity.Payment;
import com.banking.entity.PaymentHistory;
import com.banking.entity.PaymentStatus;
import com.banking.entity.User;
import com.banking.repository.PaymentHistoryRepository;
import com.banking.repository.PaymentRepository;
import com.banking.repository.UserRepository;
import com.banking.service.CardService;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;

import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/razorpay")
public class PaymentController extends BaseController{

    @Autowired
    private PaymentRepository paymentRepository;
    
    @Autowired
    private PaymentHistoryRepository paymentHistoryRepository;

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private CardService cardService;


    @Value("${razorpay.key}")
    private String razorpayKey;

    @Value("${razorpay.secret}")
    private String razorpaySecret;

    private RazorpayClient razorpayClient;

    // ✅ Initialize safely after properties are injected
    @PostConstruct
    public void init() {
        try {
            System.out.println("🔑 Razorpay Key: " + razorpayKey);
            System.out.println("🧩 Razorpay Secret: " + razorpaySecret);
            razorpayClient = new RazorpayClient(razorpayKey, razorpaySecret);
            System.out.println("✅ Razorpay client initialized successfully");
        } catch (RazorpayException e) {
            System.err.println("❌ Razorpay initialization failed: " + e.getMessage());
        }
    }

    /** ✅ Show payment page */
   

    /** ✅ Create order API */
    @PostMapping("/create-order")
    @ResponseBody
    public Map<String, Object> createOrder(@RequestBody Map<String, Object> data) throws RazorpayException {
        double inputAmount = Double.parseDouble(data.get("amount").toString());
        String description = data.get("description").toString();
        int amountInPaise = (int) (inputAmount * 100);

        JSONObject orderRequest = new JSONObject();
        orderRequest.put("amount", amountInPaise);
        orderRequest.put("currency", "INR");
        orderRequest.put("receipt", "txn_" + System.currentTimeMillis());
        orderRequest.put("payment_capture", 1);

        Order order = razorpayClient.orders.create(orderRequest);

        Map<String, Object> response = new HashMap<>();
        response.put("id", order.get("id"));
        response.put("amount", order.get("amount"));
        response.put("currency", "INR");
        return response;
    }
    @GetMapping("/page")
    public String showPaymentPage(Model model) {
    	   
    	    System.out.println("📤 Sending key to frontend: " + razorpayKey);
    	    model.addAttribute("razorpayKey", razorpayKey);
    	    return "payment"; // Must be rendered by Spring (Thymeleaf)
    }
    
   


    /** ✅ Save payment info to DB */
 
    @PostMapping("/save-payment")
    @ResponseBody
    public ResponseEntity<String> savePayment(@RequestBody Map<String, Object> paymentData, HttpSession session) {
        
    	User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not logged in");
        }
        
		  // Re-fetch from DB to ensure it's managed User user =
		User user =  userRepository.findById(loggedInUser.getId()).orElse(null); 
		if (user == null){
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
		  }
		

        // ✅ Create and populate Payment entity
        Payment payment = new Payment();
        payment.setPaymentId(paymentData.get("paymentId").toString());
        payment.setOrderId(paymentData.get("orderId").toString());
        payment.setAmount(Double.parseDouble(paymentData.get("amount").toString()));
        payment.setDescription(paymentData.get("description").toString());
        payment.setStatus("Success");
        payment.setPaymentDate(LocalDateTime.now());
        payment.setUser(user);
        paymentRepository.save(payment);
        
        if ("Loan EMI".equalsIgnoreCase(payment.getDescription())
                || "Credit Card Bill".equalsIgnoreCase(payment.getDescription())) {

                
        	Long creditAccountId =
        		    Long.valueOf(paymentData.get("creditAccountId").toString());

        		PaymentHistory ph =
        		    paymentHistoryRepository.findLatestPending(user.getId(), creditAccountId);

                if (ph != null) {
                    ph.setPaymentDate(LocalDate.now());

                    if (ph.getPaymentDate().isAfter(ph.getDueDate())) {
                        ph.setStatus(PaymentStatus.LATE);
                    } else {
                        ph.setStatus(PaymentStatus.PAID);
                    }

                    paymentHistoryRepository.save(ph);
                }
            }

        return ResponseEntity.ok("Payment saved successfully");
    }

    @GetMapping("/payment")
    public String redirectToPaymentPage(HttpSession session) {
    	if (!isLoggedIn(session)) return "redirect:/login";

        return "redirect:/razorpay/page";
    }


    
}
