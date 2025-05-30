package com.financialapplication.expansesanalysis.Model.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
public class Category {

@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

private double groceries;
private double medical;
private double domestic;
private double shopping;
private double bills;
private double entertainment;
private double travelling;
private double fueling;
private double educational;
private double others;


@JsonIgnore
@OneToOne
@JoinColumn(name = "user_id")
private User user;



}
