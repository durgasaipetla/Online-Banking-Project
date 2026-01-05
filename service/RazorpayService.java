package com.banking.service;

import org.json.JSONObject;
import org.springframework.stereotype.Service;

import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;

import jakarta.annotation.PostConstruct;

@Service
public class RazorpayService {

    private RazorpayClient razorpayClient;

    @PostConstruct
    public void init() throws RazorpayException {
        razorpayClient = new RazorpayClient(
            "RAZORPAY_KEY",
            "RAZORPAY_SECRET"
        );
    }

    public Order createOrder(double amount) throws RazorpayException {

        JSONObject options = new JSONObject();
        options.put("amount", (int) (amount * 100)); // paise
        options.put("currency", "INR");
        options.put("receipt", "emi_" + System.currentTimeMillis());

        return razorpayClient.orders.create(options);
    }
}
