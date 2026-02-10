package com.financialapplication.tijori.Service;

import com.financialapplication.tijori.Config.CacheConfig;
import com.financialapplication.tijori.Exception.UserAlreadyExistException;
import com.financialapplication.tijori.Exception.NotFoundException;
import com.financialapplication.tijori.Model.DTO.UserProfileDto;
import com.financialapplication.tijori.Model.Entity.AccountBalance;
import com.financialapplication.tijori.Model.Entity.User;
import com.financialapplication.tijori.Model.Request.RegisterRequest;
import com.financialapplication.tijori.Repository.AccountBalanceRepository;
import com.financialapplication.tijori.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final ModelMapper mapper;
    private final AccountBalanceRepository accountBalanceRepository;

    @Override
    @Transactional
    public User signUpUser(RegisterRequest registerRequest) throws UserAlreadyExistException {
        log.info("Attempting to register user with mobile: {}", registerRequest.getMobileNo());
        if (userRepository.findByMobile(registerRequest.getMobileNo()).isPresent()) {
            throw new UserAlreadyExistException("User already exists with mobile number: " + registerRequest.getMobileNo());
        }
        if (userRepository.findByEmail(registerRequest.getEmail()).isPresent()) {
            throw new UserAlreadyExistException("User already exists with email address: " + registerRequest.getEmail());
        }
        User user = mapper.map(registerRequest, User.class);
        User savedUser = userRepository.save(user);

        AccountBalance accountBalance = new AccountBalance();
        accountBalance.setUser(savedUser);
        accountBalance.setTotalCredits(0.0);
        accountBalance.setTotalDebits(0.0);
        accountBalance.setTotalSavings(0.0);
        accountBalance.setIncome(0.0);
        accountBalance.setMonthlyLimit(0.0);
        accountBalance.setBudgetLimit(0.0);
        accountBalanceRepository.save(accountBalance);

        log.info("User registered successfully with mobile: {}", registerRequest.getMobileNo());
        return savedUser;
    }

    @Override
    public User loginUser(String mobileNo) throws NotFoundException {
        log.info("Attempting to log in user with mobile: {}", mobileNo);
        return findUserByMobile(mobileNo);
    }




    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = CacheConfig.USER_PROFILE_CACHE, key = "#mobileNo")
    public UserProfileDto getProfile(String mobileNo) throws NotFoundException {
        log.info("Fetching profile for user with mobile: {}", mobileNo);
        User user = userRepository.findByMobile(mobileNo)
                .orElseThrow(() -> new NotFoundException("User not found with mobile number: " + mobileNo));

        AccountBalance accountBalance = accountBalanceRepository.findAccountBalanceByUser_Id(user.getId())
                .orElseThrow(() -> new NotFoundException("Financial details not found for user with mobile: " + mobileNo));

        UserProfileDto profile = mapper.map(user, UserProfileDto.class);

        profile.setIncome(Optional.ofNullable(accountBalance.getIncome()).orElse(0.0));
        profile.setTotalDebits(Optional.ofNullable(accountBalance.getTotalDebits()).orElse(0.0));
        profile.setTotalSavings(Optional.ofNullable(accountBalance.getTotalSavings()).orElse(0.0));
        profile.setMonthlyLimit(Optional.ofNullable(accountBalance.getMonthlyLimit()).orElse(0.0));
        profile.setBudgetLimit(Optional.ofNullable(accountBalance.getBudgetLimit()).orElse(0.0));

        log.info("Profile fetched successfully for user with mobile: {}", mobileNo);
        return profile;
    }

    // Reusable method to fetch user by mobile number
    private User findUserByMobile(String mobileNo) throws NotFoundException {
        return userRepository.findByMobile(mobileNo)
                .orElseThrow(() -> new NotFoundException("User not found with mobile number: " + mobileNo));
    }
}