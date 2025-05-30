package com.financialapplication.expansesanalysis.Model.Response;

import com.financialapplication.expansesanalysis.Model.Entity.Sms;
import lombok.Data;

import java.util.List;

@Data
public class SmsResponse {

    private String message;
    private List<Sms> smsList;
    private boolean status;

    public SmsResponse(String message, boolean status, List<Sms> smsList) {
        this.message = message;
        this.status = status;
        this.smsList = smsList;
    }

    public SmsResponse() {

    }
}
