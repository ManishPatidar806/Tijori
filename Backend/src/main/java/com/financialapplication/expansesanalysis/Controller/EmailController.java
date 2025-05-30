package com.financialapplication.expansesanalysis.Controller;


import com.financialapplication.expansesanalysis.Service.EmailService;
import org.springframework.web.bind.annotation.*;

/*
This controller is can be used when we want to verify email
 */

@RestController
@RequestMapping("/api/emailVerification")
public class EmailController {

private final EmailService emailService;

    public EmailController(EmailService emailService) {
        this.emailService = emailService;
    }

    @GetMapping("/sendOtp")
    public String sendOtp(@RequestParam String email) {
        emailService.generateAndSendOtp(email);
        return "OTP sent to " + email;
    }

    @GetMapping("/verifyOtp")
    public String verifyOtp(@RequestParam String email, @RequestParam String otp) {
        boolean isValid = emailService.verifyOtp(email, otp);
        return isValid ? "OTP Verified Successfully!" : "Invalid or Expired OTP!";
    }



}
