package com.financialapplication.expansesanalysis.Model.Dto;

import com.financialapplication.expansesanalysis.Model.Enum.CategoryType;
import com.financialapplication.expansesanalysis.Model.Enum.MoneyType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SmsUpdate {

    @NotNull(message = "Id must be valid")
    private Long id;
    @NotNull(message = "Amount is not Present")
    private Double amount;
    @NotBlank(message = "RefNo is not Present")
    private String refNo;

    @NotNull(message = "Category Type is not present")
    @Enumerated(EnumType.STRING)
    private CategoryType category;

    @NotNull(message = "Money Type is not present")
    @Enumerated(EnumType.STRING)
    private MoneyType moneyType;

    @NotNull(message = "TimeAndDate is Not present in Proper Format")
    private LocalDateTime dateTime;


}


