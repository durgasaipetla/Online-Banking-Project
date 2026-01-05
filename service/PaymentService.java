
package com.banking.service;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.banking.entity.Payment;
import com.banking.entity.PaymentHistory;
import com.banking.entity.PaymentStatus;
import com.banking.entity.User;
import com.banking.repository.PaymentHistoryRepository;
import com.banking.repository.PaymentRepository;
import com.banking.repository.UserRepository;

@Service
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private PaymentHistoryRepository paymentHistoryRepository;

    @Autowired
    private UserRepository userRepository;

    /**
     * Called AFTER Razorpay SUCCESS
     */
    public void handleSuccessfulPayment(
            String transactionId,
            String description,
            double amount,
            Long userId,
            Long creditAccountId
    ) {

        // 1️⃣ Save Razorpay transaction
        User user = userRepository.findById(userId)
                .orElseThrow();

        Payment payment = new Payment();
        payment.setPaymentId(transactionId);
        payment.setDescription(description);
        payment.setAmount(amount);
        payment.setStatus("SUCCESS");
        payment.setPaymentDate(LocalDateTime.now());
        payment.setUser(user);

        paymentRepository.save(payment);

        // 2️⃣ Update CIBIL Payment History
        PaymentHistory history =
                paymentHistoryRepository.findLatestPending(userId, creditAccountId);
        LocalDate paymentDate = LocalDate.now();
        
        history.setPaymentDate(paymentDate);

        if (!history.getPaymentDate().isAfter(history.getDueDate())) {
            history.setStatus(PaymentStatus.PAID);
        } else {
            history.setStatus(PaymentStatus.LATE);
        }

        paymentHistoryRepository.save(history);
    }

    public List<Payment> getPaymentsByUserId(Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return paymentRepository.findByUser_Id(userId);
    }

}



