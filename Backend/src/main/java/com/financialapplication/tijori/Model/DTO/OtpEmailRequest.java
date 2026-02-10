package com.financialapplication.tijori.Model.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO for OTP email request.
 */
@Setter
public class OtpEmailRequest {

    @Getter
    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @Getter
    @NotBlank(message = "OTP is required")
    private String otp;

    private String userName;

    public OtpEmailRequest() {
    }

    public OtpEmailRequest(String email, String otp) {
        this.email = email;
        this.otp = otp;
    }

    public OtpEmailRequest(String email, String otp, String userName) {
        this.email = email;
        this.otp = otp;
        this.userName = userName;
    }

    public String getUserName() {
        return userName != null ? userName : "User";
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String email;
        private String otp;
        private String userName;

        public Builder email(String email) {
            this.email = email;
            return this;
        }

        public Builder otp(String otp) {
            this.otp = otp;
            return this;
        }

        public Builder userName(String userName) {
            this.userName = userName;
            return this;
        }

        public OtpEmailRequest build() {
            return new OtpEmailRequest(email, otp, userName);
        }
    }
}

