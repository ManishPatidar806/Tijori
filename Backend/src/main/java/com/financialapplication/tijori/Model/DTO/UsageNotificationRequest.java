package com.financialapplication.tijori.Model.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * DTO for usage notification email request.
 */
@Setter
@Getter
public class UsageNotificationRequest {

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "User name is required")
    private String userName;

    @Positive(message = "Used amount must be positive")
    private BigDecimal usedAmount;

    @Positive(message = "Total amount must be positive")
    private BigDecimal totalAmount;

    public UsageNotificationRequest() {
    }

    public UsageNotificationRequest(String email, String userName, BigDecimal usedAmount, BigDecimal totalAmount) {
        this.email = email;
        this.userName = userName;
        this.usedAmount = usedAmount;
        this.totalAmount = totalAmount;
    }

    /**
     * Calculate usage percentage.
     */
    public int getUsagePercentage() {
        if (totalAmount == null || totalAmount.compareTo(BigDecimal.ZERO) == 0) {
            return 0;
        }
        return usedAmount.multiply(BigDecimal.valueOf(100))
                .divide(totalAmount, 0, java.math.RoundingMode.HALF_UP)
                .intValue();
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String email;
        private String userName;
        private BigDecimal usedAmount;
        private BigDecimal totalAmount;

        public Builder email(String email) {
            this.email = email;
            return this;
        }

        public Builder userName(String userName) {
            this.userName = userName;
            return this;
        }

        public Builder usedAmount(BigDecimal usedAmount) {
            this.usedAmount = usedAmount;
            return this;
        }

        public Builder usedAmount(double usedAmount) {
            this.usedAmount = BigDecimal.valueOf(usedAmount);
            return this;
        }

        public Builder totalAmount(BigDecimal totalAmount) {
            this.totalAmount = totalAmount;
            return this;
        }

        public Builder totalAmount(double totalAmount) {
            this.totalAmount = BigDecimal.valueOf(totalAmount);
            return this;
        }

        public UsageNotificationRequest build() {
            return new UsageNotificationRequest(email, userName, usedAmount, totalAmount);
        }
    }
}

