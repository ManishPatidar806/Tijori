package com.financialapplication.tijori.Model.Response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(name = "AuthResponse", description = "Authentication response containing access and refresh tokens.")
public class AuthResponse {

    @Schema(description = "Human-readable result message", example = "Login successful. Welcome back, Mohit!")
    private String message;

    @Schema(description = "Whether the authentication request succeeded", example = "true")
    private Boolean status;

    @Schema(description = "Legacy access token alias", example = "eyJhbGciOiJIUzUxMiJ9...")
    private String token;

    @Schema(description = "Short-lived JWT access token", example = "eyJhbGciOiJIUzUxMiJ9...")
    private String accessToken;

    @Schema(description = "Long-lived refresh token", example = "eyJhbGciOiJIUzUxMiJ9...")
    private String refreshToken;

    @Schema(description = "Token type returned to the client", example = "Bearer")
    private String tokenType;

    @Schema(description = "Access token lifetime in milliseconds", example = "900000")
    private Long expiresIn;


}
