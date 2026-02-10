package com.financialapplication.tijori.Model.Request;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterRequest {

    @NotBlank(message = "Name must be valid")
    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    private String name;

    @NotBlank(message = "Email must be present")
    @Email(message = "Email must be in valid format")
    private String email;

    @NotBlank(message = "Bank name must be valid")
    @Size(min = 2, max = 100, message = "Bank name must be between 2 and 100 characters")
    private String bank;

    @NotBlank(message = "Mobile number must be valid")
    @Pattern(regexp = "^[0-9]{10}$", message = "Mobile number must be exactly 10 digits")
    private String mobileNo;


}
