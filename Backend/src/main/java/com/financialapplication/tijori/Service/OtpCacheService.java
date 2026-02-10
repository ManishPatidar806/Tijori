package com.financialapplication.tijori.Service;

import com.financialapplication.tijori.Config.CacheConfig;
import com.financialapplication.tijori.Model.DTO.OtpCacheData;
import org.springframework.cache.annotation.CachePut;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class OtpCacheService {


    private final PasswordEncoder passwordEncoder;
    public OtpCacheService(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }


    @CachePut(
            value = CacheConfig.EMAIL_OTP_CACHE,
            key = "#email"
    )
    public OtpCacheData storeEmailOtp(String email, String otp) {
        return new OtpCacheData(passwordEncoder.encode(otp));
    }


}
