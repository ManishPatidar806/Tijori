package com.financialapplication.tijori.Model.Request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequest {
    @NotBlank(message = "Mobile number must be valid")
    private String mobileNo;
}
