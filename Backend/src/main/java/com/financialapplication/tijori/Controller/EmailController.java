package com.financialapplication.tijori.Controller;


import com.financialapplication.tijori.Model.DTO.OtpEmailRequest;
import com.financialapplication.tijori.Model.Response.ApiResponse;
import com.financialapplication.tijori.Service.EmailService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequestMapping("/v1/api/emailVerification")
@RequiredArgsConstructor
@Tag(name = "Email Verification", description = "Email OTP verification APIs")
public class EmailController {

    private final EmailService emailService;

    @Operation(summary = "Send OTP to email", description = "Sends a 6-digit OTP to the specified email address")
    @PostMapping("/sendOtp")
    public ResponseEntity<ApiResponse<String>> sendOtp(
            @RequestParam @NotBlank @Email(message = "Invalid email format") String email) {
        String otp = emailService.generateOtp();
        emailService.sendOtpEmailAsync(OtpEmailRequest.builder().email(email).otp(otp).build());
        return ResponseEntity.ok(ApiResponse.success("OTP sent to " + email, null));
    }

    @Operation(summary = "Verify OTP", description = "Verifies the OTP sent to the email address")
    @PostMapping("/verifyOtp")
    public ResponseEntity<ApiResponse<Boolean>> verifyOtp(
            @RequestParam @NotBlank @Email(message = "Invalid email format") String email,
            @RequestParam @NotBlank @Pattern(regexp = "^[0-9]{6}$", message = "OTP must be 6 digits") String otp) {
        boolean isValid = emailService.verifyOtp(email, otp);
        return ResponseEntity.ok(ApiResponse.success("OTP Verified Successfully!", isValid));
    }

}
