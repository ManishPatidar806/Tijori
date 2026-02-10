package com.financialapplication.tijori.Config;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

/**
 * Configuration properties for email service.
 * Externalized email configuration following 12-factor app methodology.
 */
@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "app.email")
@Validated
public class EmailProperties {

    // Getters and Setters
    /**
     * Company name to display in emails
     */
    @NotBlank(message = "Company name is required")
    private String companyName = "Tijori";

    /**
     * Company address for email footer
     */
    @NotBlank(message = "Company address is required")
    private String companyAddress = "Address 540, Pithampur, Madhya Pradesh, India";

    /**
     * Support email address
     */
    @NotBlank(message = "Support email is required")
    private String supportEmail = "support@tijori.com";

    /**
     * Support website URL
     */
    private String supportUrl = "https://tijori.com/support";

    /**
     * Logo URL for email header
     */
    private String logoUrl = "https://archisketch-resources.s3.ap-northeast-2.amazonaws.com/vrstyler/1663574980688_114990/archisketch-logo";

    /**
     * Application name to display in emails
     */
    @NotBlank(message = "App name is required")
    private String appName = "Tijori";

    /**
     * OTP validity duration in minutes
     */
    @Min(value = 1, message = "OTP validity must be at least 1 minute")
    private int otpValidityMinutes = 3;

    /**
     * Maximum retry attempts for failed email sending
     */
    @Min(value = 1, message = "Max retries must be at least 1")
    private int maxRetries = 3;

    /**
     * Delay between retry attempts in milliseconds
     */
    @Min(value = 100, message = "Retry delay must be at least 100ms")
    private long retryDelayMs = 1000;

    /**
     * Enable async email sending
     */
    private boolean asyncEnabled = true;

}
