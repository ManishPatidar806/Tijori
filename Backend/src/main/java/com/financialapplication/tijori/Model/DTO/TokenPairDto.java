package com.financialapplication.tijori.Model.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "TokenPair", description = "Access token and refresh token pair returned by authentication endpoints.")
public class TokenPairDto {

    @Schema(description = "Short-lived JWT access token", example = "eyJhbGciOiJIUzUxMiJ9...")
    private String accessToken;

    @Schema(description = "Long-lived refresh token", example = "eyJhbGciOiJIUzUxMiJ9...")
    private String refreshToken;

    @Schema(description = "Access token lifetime in milliseconds", example = "900000")
    private Long expiresIn;
}