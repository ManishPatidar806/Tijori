package com.financialapplication.expansesanalysis.Service;



import org.springframework.stereotype.Service;

    @Service
    public interface EmailService  {
        public void generateAndSendOtp(String email);
        public boolean verifyOtp(String email, String otp);

        public void cleanupExpiredOtps();
    }

