package com.financialapplication.tijori.Model.Response;

import com.financialapplication.tijori.Model.Entity.AccountBalance;
import lombok.Data;

@Data
public class AccountBalanceResponse {

    private String message;
    private Boolean status;
    private AccountBalance accountBalance;

    public AccountBalanceResponse() {
    }

    public AccountBalanceResponse(String message, boolean status, AccountBalance accountBalance) {
        this.message = message;
        this.status = status;
        this.accountBalance = accountBalance;
    }
}
