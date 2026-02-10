package com.financialapplication.tijori.Controller;

import com.financialapplication.tijori.Config.RateLimitConfig;
import com.financialapplication.tijori.Config.RateLimited;
import com.financialapplication.tijori.Exception.BusinessException;
import com.financialapplication.tijori.Exception.UserAlreadyExistException;
import com.financialapplication.tijori.Exception.NotFoundException;
import com.financialapplication.tijori.Model.DTO.UserProfileDto;
import com.financialapplication.tijori.Model.Entity.User;
import com.financialapplication.tijori.Model.Request.LoginRequest;
import com.financialapplication.tijori.Model.Request.RegisterRequest;
import com.financialapplication.tijori.Model.Response.ApiResponse;
import com.financialapplication.tijori.Model.Response.AuthResponse;
import com.financialapplication.tijori.Service.AuthService;
import com.financialapplication.tijori.Util.JwtTokenProvider;
import io.micrometer.core.annotation.Timed;
import io.micrometer.core.instrument.Counter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/v1/api/users")
@Tag(name = "Authentication", description = "User authentication and profile management APIs")
public class AuthController {
    
    private final AuthService authService;
    private final JwtTokenProvider jwtTokenProvider;
    private final Counter userRegistrationCounter;
    private final Counter userLoginCounter;

    public AuthController(
            AuthService authService, 
            JwtTokenProvider jwtTokenProvider,
            Counter userRegistrationCounter,
            Counter userLoginCounter) {
        this.authService = authService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userRegistrationCounter = userRegistrationCounter;
        this.userLoginCounter = userLoginCounter;
    }

    @Operation(summary = "Register a new user",
            description = "Creates a new user account.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "User registered successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid input or user exists"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "429", description = "Too many requests")
    })
    @PostMapping("/signup")
    @RateLimited(bucketType = RateLimitConfig.BucketType.AUTH)
    @Timed(value = "auth.signup.time", description = "Time taken to register a user")
    public ResponseEntity<ApiResponse<AuthResponse>> registerUser(@RequestBody @Valid RegisterRequest registerRequest) 
            throws UserAlreadyExistException, NotFoundException {
        String mobileNo = registerRequest.getMobileNo();
        log.info("Processing registration request for mobile: {}", mobileNo);

        User user = authService.signUpUser(registerRequest);
        String token = jwtTokenProvider.generateToken(user.getMobile(), user.getEmail());

        AuthResponse authResponse = new AuthResponse();
        authResponse.setMessage("Registration successful. Welcome to the platform!");
        authResponse.setStatus(true);
        authResponse.setToken(token);
        
        userRegistrationCounter.increment();
        log.info("User registered successfully: {}", user.getMobile());
        
        return new ResponseEntity<>(
                ApiResponse.success("Registration successful. Welcome to the platform!", authResponse),
                HttpStatus.CREATED
        );
    }

    @Operation(summary = "Login user",
            description = "Authenticates user with mobile number.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Login successful"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "User not found"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "429", description = "Too many requests")
    })
    @PostMapping("/signin")
    @RateLimited(bucketType = RateLimitConfig.BucketType.AUTH)
    @Timed(value = "auth.signin.time", description = "Time taken to login a user")
    public ResponseEntity<ApiResponse<AuthResponse>> loginUser(@RequestBody @Valid LoginRequest loginDto) 
            throws BusinessException, NotFoundException {
        String mobile = loginDto.getMobileNo();
        log.info("Processing login request for mobile: {}", mobile);

        User user = authService.loginUser(mobile);
        String token = jwtTokenProvider.generateToken(user.getMobile(), user.getEmail());

        AuthResponse authResponse = new AuthResponse();
        authResponse.setMessage("Login successful. Welcome back, " + user.getName() + "!");
        authResponse.setStatus(true);
        authResponse.setToken(token);
        
        userLoginCounter.increment();
        log.info("User logged in successfully: {}", mobile);
        
        return new ResponseEntity<>(
                ApiResponse.success("Login successful. Welcome back, " + user.getName() + "!", authResponse),
                HttpStatus.OK
        );
    }

    @Operation(summary = "Get user profile", description = "Retrieves the authenticated user's profile information")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Profile retrieved successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized - Invalid or missing token"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "User not found")
    })
    @GetMapping("/profile")
    @Timed(value = "auth.profile.time", description = "Time taken to fetch user profile")
    public ResponseEntity<ApiResponse<UserProfileDto>> getProfile(@AuthenticationPrincipal UserDetails userDetails)
            throws NotFoundException {
        log.debug("Fetching profile for user: {}", userDetails.getUsername());
        
        UserProfileDto profile = authService.getProfile(userDetails.getUsername());

        return new ResponseEntity<>(
                ApiResponse.success("Profile retrieved successfully.", profile),
                HttpStatus.OK
        );
    }
}
