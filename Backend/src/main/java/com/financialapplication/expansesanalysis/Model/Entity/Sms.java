package com.financialapplication.expansesanalysis.Model.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.financialapplication.expansesanalysis.Model.Enum.CategoryType;
import com.financialapplication.expansesanalysis.Model.Enum.MoneyType;
import com.financialapplication.expansesanalysis.Model.Response.CommonResponse;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
public class Sms {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private double amount;

    private String refNo;


    @Enumerated(EnumType.STRING)
    private CategoryType category;


    @Enumerated(EnumType.STRING)
    private MoneyType moneyType;


    private LocalDateTime dateTime;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;




}
