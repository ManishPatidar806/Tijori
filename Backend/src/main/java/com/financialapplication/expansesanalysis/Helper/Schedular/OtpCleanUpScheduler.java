package com.financialapplication.expansesanalysis.Helper.Schedular;



import com.financialapplication.expansesanalysis.Service.EmailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@EnableScheduling
@Component
public class OtpCleanUpScheduler {


    private final EmailService emailService;

    public OtpCleanUpScheduler(EmailService emailService) {
        this.emailService = emailService;
    }

    @Scheduled(fixedRate = 120000) // Run every 3 minutes
    public void cleanExpiredOtps() {
        emailService.cleanupExpiredOtps();
        log.info("Expired OTPs cleaned up.");
    }
}