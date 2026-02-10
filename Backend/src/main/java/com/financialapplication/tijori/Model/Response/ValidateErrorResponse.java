package com.financialapplication.tijori.Model.Response;

import lombok.Data;

import java.util.Map;

@Data
public class ValidateErrorResponse {
    private Map<String ,String> message;
    private Boolean status ;

}