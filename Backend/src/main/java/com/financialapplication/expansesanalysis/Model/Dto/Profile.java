package com.financialapplication.expansesanalysis.Model.Dto;

import lombok.Data;

@Data
public class Profile {
    private String name;
    private String email;
    private String mobile;
    private String bank;
    private double savingAmount;
    private double income;
    private double monthlyLimit;
    private double debitedAmount;
}
