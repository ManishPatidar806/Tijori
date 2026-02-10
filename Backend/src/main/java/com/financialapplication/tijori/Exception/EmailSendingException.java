package com.financialapplication.tijori.Exception;

import lombok.Getter;

/**
 * Custom exception for email sending failures.
 * Provides detailed context for email operation failures.
 */
@Getter
public class EmailSendingException extends RuntimeException {

    private final String recipient;
    private final String emailType;
    private final int attemptCount;

    public EmailSendingException(String message, String recipient, String emailType) {
        super(message);
        this.recipient = recipient;
        this.emailType = emailType;
        this.attemptCount = 1;
    }

    public EmailSendingException(String message, String recipient, String emailType, int attemptCount) {
        super(message);
        this.recipient = recipient;
        this.emailType = emailType;
        this.attemptCount = attemptCount;
    }

    public EmailSendingException(String message, String recipient, String emailType, Throwable cause) {
        super(message, cause);
        this.recipient = recipient;
        this.emailType = emailType;
        this.attemptCount = 1;
    }

    public EmailSendingException(String message, String recipient, String emailType, int attemptCount, Throwable cause) {
        super(message, cause);
        this.recipient = recipient;
        this.emailType = emailType;
        this.attemptCount = attemptCount;
    }

    @Override
    public String toString() {
        return String.format("EmailSendingException{recipient='%s', emailType='%s', attemptCount=%d, message='%s'}",
                maskEmail(recipient), emailType, attemptCount, getMessage());
    }

    /**
     * Masks email for logging to comply with privacy requirements.
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
}

