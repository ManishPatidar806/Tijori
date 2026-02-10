package com.financialapplication.tijori.Model.Response;

import lombok.Data;

@Data
public class AuthResponse {

    private String message;
    private Boolean status;
    private String token;


}
