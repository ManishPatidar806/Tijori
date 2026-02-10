package com.financialapplication.tijori.Service;

import com.financialapplication.tijori.Config.CacheConfig;
import com.financialapplication.tijori.Config.EmailProperties;
import com.financialapplication.tijori.Exception.BusinessException;
import com.financialapplication.tijori.Exception.EmailSendingException;
import com.financialapplication.tijori.Model.DTO.OtpCacheData;
import com.financialapplication.tijori.Model.DTO.OtpEmailRequest;
import com.financialapplication.tijori.Model.DTO.UsageNotificationRequest;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.Year;
import java.time.format.DateTimeFormatter;
import java.util.Locale;


@Slf4j
@Service
public class EmailServiceImpl implements EmailService {

    private static final String OTP_TEMPLATE = "email/otp-verification";
    private static final String USAGE_NOTIFICATION_TEMPLATE = "email/usage-notification";
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;
    private final EmailProperties emailProperties;
    private final SecureRandom secureRandom;
    private final CacheManager cacheManager;
    private final PasswordEncoder passwordEncoder;

    // Metrics
    private final Counter emailsSentCounter;
    private final Counter emailsFailedCounter;
    private final Timer emailSendTimer;
    private final OtpCacheService otpCacheService;

    public EmailServiceImpl(
            JavaMailSender mailSender,
            TemplateEngine templateEngine,
            EmailProperties emailProperties,
            CacheManager cacheManager,
            PasswordEncoder passwordEncoder,
            MeterRegistry meterRegistry,
            OtpCacheService otpCacheService) {
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
        this.emailProperties = emailProperties;
        this.cacheManager = cacheManager;
        this.passwordEncoder = passwordEncoder;
        this.otpCacheService = otpCacheService;
        this.secureRandom = new SecureRandom();

        this.emailsSentCounter = Counter.builder("email.sent.total")
                .description("Emails sent")
                .register(meterRegistry);
        this.emailsFailedCounter = Counter.builder("email.failed.total")
                .description("Failed emails")
                .register(meterRegistry);
        this.emailSendTimer = Timer.builder("email.send.duration")
                .description("Email send time")
                .register(meterRegistry);
    }


    @Override
    @Async("emailExecutor")
    public void sendOtpEmailAsync(OtpEmailRequest request) {
        sendOtpEmail(request);

    }

    @Override
    public void sendUsageNotificationEmail(UsageNotificationRequest request) {
        log.info("Sending usage notification email to: {}", maskEmail(request.getEmail()));

        Context context = new Context(Locale.ENGLISH);
        context.setVariable("userName", request.getUserName());
        context.setVariable("usedAmount", request.getUsedAmount());
        context.setVariable("totalAmount", request.getTotalAmount());
        context.setVariable("usagePercentage", request.getUsagePercentage());
        context.setVariable("currentYear", Year.now().getValue());
        context.setVariable("companyName", emailProperties.getCompanyName());
        context.setVariable("supportEmail", emailProperties.getSupportEmail());

        String htmlContent = templateEngine.process(USAGE_NOTIFICATION_TEMPLATE, context);
        String subject = "Important Notification - Your Monthly Limit Alert!";

        sendEmailWithRetry(request.getEmail(), subject, htmlContent, "USAGE_NOTIFICATION");
    }

    @Override
    @Async("emailExecutor")
    public void sendUsageNotificationEmailAsync(UsageNotificationRequest request) {
        sendUsageNotificationEmail(request);
    }

    /**
     * We have not use this method currently
     * */
    @Override
    public void sendHtmlEmail(String to, String subject, String htmlContent) {
        sendEmailWithRetry(to, subject, htmlContent, "GENERIC");
    }

    /**
     * Send email with retry mechanism and exponential backoff.
     */
    private void sendEmailWithRetry(String to, String subject, String htmlContent, String emailType) {
        int maxRetries = emailProperties.getMaxRetries();
        long retryDelay = emailProperties.getRetryDelayMs();
        Exception lastException = null;

        for (int attempt = 1; attempt <= maxRetries; attempt++) {
            try {
                emailSendTimer.record(() -> {
                    try {
                        doSendEmail(to, subject, htmlContent);
                    } catch (MessagingException e) {
                        throw new RuntimeException(e);
                    }
                });

                emailsSentCounter.increment();
                log.info("Email sent successfully. Type: {}, Recipient: {}, Attempt: {}",
                        emailType, maskEmail(to), attempt);
                return;

            } catch (RuntimeException e) {
                lastException = e;
                emailsFailedCounter.increment();

                log.warn("Failed to send email. Type: {}, Recipient: {}, Attempt: {}/{}, Error: {}",
                        emailType, maskEmail(to), attempt, maxRetries, e.getMessage());

                if (attempt < maxRetries) {
                    try {
                        // Exponential backoff
                        long sleepTime = retryDelay * (long) Math.pow(2, attempt - 1);
                        Thread.sleep(sleepTime);
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                        throw new EmailSendingException(
                                "Email sending interrupted",
                                to, emailType, attempt, ie);
                    }
                }
            }
        }

        // All retries exhausted
        log.error("Failed to send email after {} attempts. Type: {}, Recipient: {}",
                maxRetries, emailType, maskEmail(to));
        throw new EmailSendingException(
                "Failed to send email after " + maxRetries + " attempts",
                to, emailType, maxRetries, lastException);
    }

    /**
     * Internal method to send email.
     */
    private void doSendEmail(String to, String subject, String htmlContent) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(htmlContent, true);

        mailSender.send(message);
    }

    /**
     * Mask email for logging (GDPR compliance).
     */
    private String maskEmail(String email) {
        if (email == null || !email.contains("@")) {
            return "***";
        }
        int atIndex = email.indexOf('@');
        if (atIndex <= 2) {
            return "***" + email.substring(atIndex);
        }
        return email.substring(0, 2) + "***" + email.substring(atIndex);
    }


    public boolean verifyOtp(String email, String userOtp) {
        Cache cache = cacheManager.getCache(CacheConfig.EMAIL_OTP_CACHE);
        if (cache == null) {
            throw new IllegalStateException("OTP cache is not configured");
        }
        OtpCacheData otp  =  cache.get(email,OtpCacheData.class);
        if (otp == null) {
            log.warn("OTP expired or not found for {}", maskEmail(email));
            throw new BusinessException("OTP expired. Please request a new OTP.");
        }
        if(!passwordEncoder.matches(userOtp,otp.getHashedOtp())){
            log.warn("Invalid OTP attempt for {}", maskEmail(email));
            throw new BusinessException("Invalid OTP. Please try again.");
        }
        cache.evict(email);
        return true;
    }


    @Override
    public String generateOtp() {
        // Generate cryptographically secure 6-digit OTP
        int otp = 100000 + secureRandom.nextInt(900000);
        log.debug("Generated new OTP");
        return String.valueOf(otp);
    }

    @Override
    public void sendOtpEmail(OtpEmailRequest request) {
        log.info("Sending OTP email to: {}", maskEmail(request.getEmail()));

        Context context = new Context(Locale.ENGLISH);
        context.setVariable("otp", request.getOtp());
        context.setVariable("userName", request.getUserName());
        context.setVariable("currentDateTime", DATE_TIME_FORMATTER.format(LocalDateTime.now()));
        context.setVariable("currentYear", Year.now().getValue());
        context.setVariable("otpValidityMinutes", emailProperties.getOtpValidityMinutes());
        context.setVariable("appName", emailProperties.getAppName());
        context.setVariable("companyName", emailProperties.getCompanyName());
        context.setVariable("companyAddress", emailProperties.getCompanyAddress());
        context.setVariable("supportUrl", emailProperties.getSupportUrl());
        context.setVariable("logoUrl", emailProperties.getLogoUrl());
        String htmlContent = templateEngine.process(OTP_TEMPLATE, context);
        String subject = "🚀 Verify Your Account - Your OTP Code Inside!";
        sendEmailWithRetry(request.getEmail(), subject, htmlContent, "OTP");
        otpCacheService.storeEmailOtp(request.getEmail(), request.getOtp());
    }



}

