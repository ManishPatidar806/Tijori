package com.financialapplication.tijori.Model.Response;

import com.financialapplication.tijori.Model.Entity.Transaction;
import lombok.Data;

import java.util.List;

@Data
public class TransactionResponse {

    private String message;
    private List<Transaction> transactions;
    private Boolean status;

    public TransactionResponse(String message, boolean status, List<Transaction> transactions) {
        this.message = message;
        this.status = status;
        this.transactions = transactions;
    }

    public TransactionResponse() {
    }
}
