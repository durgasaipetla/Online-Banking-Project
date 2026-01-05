package com.banking.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

@Service
public class OtpService {

    private Map<String, OtpEntry> otpStorage = new HashMap<>();
    // OTP validity: 5 minutes = 5 * 60 * 1000 ms
    private static final long OTP_VALIDITY_DURATION = 10 * 60 * 1000;

    // Class to store OTP and when it was created
    private static class OtpEntry {
        private final String otp;
        private final long timestamp;

        public OtpEntry(String otp, long timestamp) {
            this.otp = otp;
            this.timestamp = timestamp;
        }

        public String getOtp() {
            return otp;
        }

        public long getTimestamp() {
            return timestamp;
        }
    }

    /** Generate and store OTP */
    public String generateOTP(String email) {
        int otp = (int) (Math.random() * 900000) + 100000; // 6 digits
        String otpStr = String.valueOf(otp);

        otpStorage.put(email, new OtpEntry(otpStr, System.currentTimeMillis()));

        return otpStr;
    }

    /** Validate OTP — check value and 5-min expiry */
    public boolean validateOtp(String email, String enteredOtp) {
        OtpEntry otpEntry = otpStorage.get(email);

        if (otpEntry == null) {
            return false; 
        }
        long currentTime = System.currentTimeMillis();

        // Check if expired
        if (currentTime - otpEntry.getTimestamp() > OTP_VALIDITY_DURATION) {
            otpStorage.remove(email); // remove expired OTP
            return false;
        }

        // Match OTP value
        boolean isValid = otpEntry.getOtp().equals(enteredOtp);

        if (isValid) {
            otpStorage.remove(email);
        }

        return isValid;
    }
}
