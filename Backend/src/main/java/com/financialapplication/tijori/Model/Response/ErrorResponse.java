package com.financialapplication.tijori.Model.Response;


import lombok.Data;
@Data
public class ErrorResponse {
    private String message;
    private Boolean status;

}