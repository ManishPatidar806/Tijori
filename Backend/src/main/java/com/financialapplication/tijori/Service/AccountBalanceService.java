package com.financialapplication.tijori.Service;

import com.financialapplication.tijori.Exception.NotFoundException;

/**
 * Service interface for account balance and budget management.
 */
public interface AccountBalanceService {

    /**
     * Saves income and savings for a user, calculating the monthly spending limit.
     *
     * @param mobileNo The user's mobile number
     * @param saving   Monthly savings amount
     * @param income   Monthly income amount
     * @throws NotFoundException if user not found
     */
    void saveIncome(String mobileNo, double saving, double income) throws NotFoundException;
}
