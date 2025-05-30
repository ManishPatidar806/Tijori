package com.financialapplication.expansesanalysis.Service;

import com.financialapplication.expansesanalysis.Exception.NotFoundException;
import com.financialapplication.expansesanalysis.Model.Entity.Money;
import com.financialapplication.expansesanalysis.Model.Response.CommonResponse;
import com.financialapplication.expansesanalysis.Repository.MoneyRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;

@Service
public class MoneyServiceImpl implements MoneyService {

    private final MoneyRepository moneyRepository;
    private final Logger logger = Logger.getLogger(getClass().getName());

    public MoneyServiceImpl(MoneyRepository moneyRepository) {
        this.moneyRepository = moneyRepository;
    }

    @Override
    public ResponseEntity<CommonResponse> saveIncome(String mobileNo, double saving, double income) throws NotFoundException {
        logger.info("Saving income for mobile number: " + mobileNo);

        // Validate input
        if (mobileNo == null || mobileNo.trim().isEmpty()) {
            logger.warning("Invalid mobile number provided.");
            return new ResponseEntity<>(new CommonResponse("Mobile number cannot be null or empty.", false), HttpStatus.BAD_REQUEST);
        }
        if (saving < 0 || income < 0) {
            logger.warning("Invalid saving or income values provided.");
            return new ResponseEntity<>(new CommonResponse("Saving and income must be positive values.", false), HttpStatus.BAD_REQUEST);
        }
        if (income <= saving) {
            logger.warning("Income is less than or equal to saving.");
            return new ResponseEntity<>(new CommonResponse("Income must be greater than saving.", false), HttpStatus.BAD_REQUEST);
        }

        // Fetch Money entity
        Money money = moneyRepository.findAmountByMoblieNo(mobileNo)
                .orElseThrow(() -> new NotFoundException("Money details not found for mobile number: " + mobileNo));

        // Update Money entity
        money.setIncome(income);
        money.setSavingAmount(saving);
        money.setMonthlyLimit(income - saving);
        money.setFixedLimit(income-saving);
        moneyRepository.save(money);

        double limit = income - saving;
        logger.info("Income and saving updated successfully for mobile number: " + mobileNo);

        // Return response
        return new ResponseEntity<>(new CommonResponse("Monthly limit set successfully: Rs. " + limit, true), HttpStatus.OK);
    }
}