package com.financialapplication.tijori.Model.Request;


import com.financialapplication.tijori.Model.Enum.TransactionType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class TransactionSaveRequest {

    @Getter
    @Setter
    public static class TransactionData {

        @NotNull(message = "Amount is not Present")
        private Double amount;

        @NotBlank(message = "Reference Number is not Present")
        private String referenceNumber;


        @NotNull(message = "Transaction Type is not present")
        @Enumerated(EnumType.STRING)
        private TransactionType transactionType;

        @NotNull(message = "TimeAndDate is Not present in Proper Format")
        private LocalDateTime dateTime;

    }


    private List<TransactionData> transactions;
}
