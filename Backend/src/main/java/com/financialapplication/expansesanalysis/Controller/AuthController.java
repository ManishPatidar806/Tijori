package com.financialapplication.expansesanalysis.Controller;

import com.financialapplication.expansesanalysis.Exception.CommonException;
import com.financialapplication.expansesanalysis.Exception.UserAlreadyExistException;
import com.financialapplication.expansesanalysis.Exception.NotFoundException;
import com.financialapplication.expansesanalysis.Model.Entity.User;
import com.financialapplication.expansesanalysis.Model.Request.LoginRequest;
import com.financialapplication.expansesanalysis.Model.Request.RegisterRequest;
import com.financialapplication.expansesanalysis.Model.Response.AuthResponse;
import com.financialapplication.expansesanalysis.Model.Response.CommonResponse;
import com.financialapplication.expansesanalysis.Service.AuthService;
import com.financialapplication.expansesanalysis.Util.JwtSecurity;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class AuthController {
    private final AuthService authService;
    private final JwtSecurity jwtSecurity;

    public AuthController(AuthService authService, JwtSecurity jwtSecurity) {
        this.authService = authService;
        this.jwtSecurity = jwtSecurity;
    }


    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> registerUser(@RequestBody @Valid RegisterRequest  registerRequest) throws UserAlreadyExistException, NotFoundException {
        User user1 = authService.signUpUser(registerRequest);
        String token  = jwtSecurity.generateToken(user1.getMobile(), user1.getEmail());
        AuthResponse authResponse = new AuthResponse();
        authResponse.setMessage("Registration successful. Welcome to the platform!");
        authResponse.setStatus(true);
        authResponse.setToken(token);
        return new ResponseEntity<>(authResponse , HttpStatus.CREATED);
    }

    @PostMapping("/signin")
    public ResponseEntity<AuthResponse> loginUser(@RequestBody @Valid LoginRequest loginDto) throws CommonException, NotFoundException {
        String mobile = loginDto.getMobileNo();
        User user1 = authService.loginUser(mobile);
        AuthResponse authResponse = new AuthResponse();
        String token  = jwtSecurity.generateToken(user1.getMobile(), user1.getEmail());
        authResponse.setMessage("Login successful. Welcome back," + user1.getName() + "!");
        authResponse.setStatus(true);
        authResponse.setToken(token);
        return new ResponseEntity<>(authResponse , HttpStatus.CREATED);
    }

    @GetMapping("/profile")
    public ResponseEntity<CommonResponse> getprofile(@AuthenticationPrincipal UserDetails userDetails) throws NotFoundException {
       return authService.getProfile(userDetails.getUsername());
    }

}
