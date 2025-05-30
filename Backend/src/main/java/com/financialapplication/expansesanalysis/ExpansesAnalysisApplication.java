package com.financialapplication.expansesanalysis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.bind.annotation.RequestMapping;

@EnableScheduling
@SpringBootApplication
public class ExpansesAnalysisApplication {

	public static void main(String[] args) {
		SpringApplication.run(ExpansesAnalysisApplication.class, args);
	}

}
