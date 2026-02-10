package com.financialapplication.tijori.Model.Response;

import lombok.Data;

@Data
public class CommonResponse {

    private String message;
    private Boolean status;
    private Object data;

    public CommonResponse(String message, boolean status) {
        this.message = message;
        this.status = status;
    }

    public CommonResponse() {

    }

}
