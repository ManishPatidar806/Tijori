package com.financialapplication.tijori.Model.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OtpCacheData {

    private final String hashedOtp;

    public OtpCacheData(String hashedOtp) {
        this.hashedOtp = hashedOtp;
    }

}
