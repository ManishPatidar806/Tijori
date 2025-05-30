package com.financialapplication.expansesanalysis.Service;



import com.financialapplication.expansesanalysis.Helper.EmailSender;
import com.financialapplication.expansesanalysis.Model.Entity.OTP;
import com.financialapplication.expansesanalysis.Repository.EmailRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class EmailServiceImpl implements EmailService{


    private final EmailRepository emailRepository;

    private final EmailSender emailSender;

    public EmailServiceImpl(EmailRepository emailRepository, EmailSender emailSender) {
        this.emailRepository = emailRepository;
        this.emailSender = emailSender;
    }

    public void generateAndSendOtp(String email) {
        String otp = emailSender.generateOtp();
        LocalDateTime expiryTime = LocalDateTime.now().plusMinutes(2);

        // Save OTP to the database
        OTP otpEntity = new OTP();
        otpEntity.setEmail(email);
        otpEntity.setOtp(otp);
        otpEntity.setExpiryTime(expiryTime);
        emailRepository.save(otpEntity);

        // Send OTP via email
        emailSender.sendOtpEmail(email, otp);
    }

    public boolean verifyOtp(String email, String otp) {
        OTP storedOtp   = emailRepository.findByEmail(email);

        if (storedOtp!=null&& !storedOtp.getOtp().isEmpty() && storedOtp.getOtp().equals(otp)) {
            if (storedOtp.getExpiryTime().isAfter(LocalDateTime.now())) {
                emailRepository.delete(storedOtp); // Remove OTP after successful verification
                return true;
            }
        }
        return false;
    }

    // Cleanup expired OTPs periodically
    public void cleanupExpiredOtps() {
        emailRepository.deleteByExpiryTimeBefore(LocalDateTime.now());
    }



}

