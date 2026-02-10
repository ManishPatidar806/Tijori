package com.financialapplication.tijori.Model.DTO;

import lombok.Data;


@Data
public class UserProfileDto {
    private String name;
    private String email;
    private String mobile;
    private String bank;
    private Double totalSavings;
    private Double income;
    private Double monthlyLimit;
    private Double totalDebits;
    private Double budgetLimit;
}
