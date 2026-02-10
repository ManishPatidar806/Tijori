package com.financialapplication.tijori.Service;

import com.financialapplication.tijori.Model.DTO.OtpEmailRequest;
import com.financialapplication.tijori.Model.DTO.UsageNotificationRequest;

import java.util.concurrent.CompletableFuture;

/**
 * Email Service Interface.
 */
public interface EmailService {

    /**
     * Generate a secure OTP code.
     * @return 6-digit OTP string
     */
    String generateOtp();

    /**
     * Send OTP verification email synchronously.
     * @param request OTP email request containing recipient and OTP
     */
    void sendOtpEmail(OtpEmailRequest request);

    /**
     * Send OTP verification email asynchronously.
     * @param request OTP email request containing recipient and OTP
     */
    void sendOtpEmailAsync(OtpEmailRequest request);

    /**
     * Send usage notification email synchronously.
     * @param request Usage notification request
     */
    void sendUsageNotificationEmail(UsageNotificationRequest request);

    /**
     * Send usage notification email asynchronously.
     * @param request Usage notification request
     */
    void sendUsageNotificationEmailAsync(UsageNotificationRequest request);

    /**
     * Send generic HTML email.
     * @param to      recipient email
     * @param subject email subject
     * @param htmlContent HTML content
     */
    void sendHtmlEmail(String to, String subject, String htmlContent);
    boolean verifyOtp(String email, String userOtp);
}
