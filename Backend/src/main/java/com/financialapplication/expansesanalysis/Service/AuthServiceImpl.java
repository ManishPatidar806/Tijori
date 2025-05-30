package com.financialapplication.expansesanalysis.Service;

import com.financialapplication.expansesanalysis.Exception.CommonException;
import com.financialapplication.expansesanalysis.Exception.UserAlreadyExistException;
import com.financialapplication.expansesanalysis.Exception.NotFoundException;
import com.financialapplication.expansesanalysis.Model.Dto.Profile;
import com.financialapplication.expansesanalysis.Model.Entity.Category;
import com.financialapplication.expansesanalysis.Model.Entity.Money;
import com.financialapplication.expansesanalysis.Model.Entity.User;
import com.financialapplication.expansesanalysis.Model.Request.RegisterRequest;
import com.financialapplication.expansesanalysis.Model.Response.CommonResponse;
import com.financialapplication.expansesanalysis.Repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.logging.Logger;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final ModelMapper mapper;
    private final Logger logger = Logger.getLogger(getClass().getName());

    public AuthServiceImpl(UserRepository userRepository, ModelMapper mapper) {
        this.userRepository = userRepository;
        this.mapper = mapper;
    }

    // Reusable method to fetch user by mobile number
    private User findUserByMobile(String mobileNo) throws NotFoundException {
        return userRepository.findByMobile(mobileNo)
                .orElseThrow(() -> new NotFoundException("User not found with mobile number: " + mobileNo));
    }

    public User signUpUser(RegisterRequest registerRequest) throws UserAlreadyExistException {
        logger.info("Attempting to register user with mobile: " + registerRequest.getMobileNo());
        if (userRepository.findByMobile(registerRequest.getMobileNo()).isPresent()) {
            throw new UserAlreadyExistException("User already exists with mobile number: " + registerRequest.getMobileNo());
        }
        User user = mapper.map(registerRequest, User.class);
        Money money = new Money();
        Category category = new Category();
        money.setUser(user);
        user.setMoney(money);
        user.setCategory(category);
        user.setCreatedAt(LocalDateTime.now());
        logger.info("User registered successfully with mobile: " + registerRequest.getMobileNo());
        return userRepository.save(user);
    }

    public User loginUser(String mobileNo) throws NotFoundException {
        logger.info("Attempting to log in user with mobile: " + mobileNo);
        return findUserByMobile(mobileNo);
    }

    public User extraceUser(String mobileNo) throws NotFoundException {
        logger.info("Extracting user with mobile: " + mobileNo);
        return findUserByMobile(mobileNo);
    }

    public ResponseEntity<CommonResponse> getProfile(String mobileNo) throws NotFoundException {
        logger.info("Fetching profile for user with mobile: " + mobileNo);
        User user = findUserByMobile(mobileNo);

        if (user.getMoney() == null) {
            throw new NotFoundException("Financial details not found for user with mobile: " + mobileNo);
        }

        Profile profile = mapper.map(user, Profile.class);
        Money money = user.getMoney();

        profile.setIncome(Optional.ofNullable(money.getIncome()).orElse(0.0));
        profile.setDebitedAmount(Optional.ofNullable(money.getDebitedAmount()).orElse(0.0));
        profile.setSavingAmount(Optional.ofNullable(money.getSavingAmount()).orElse(0.0));
        profile.setMonthlyLimit(Optional.ofNullable(money.getMonthlyLimit()).orElse(0.0));

        CommonResponse commonResponse = new CommonResponse();
        commonResponse.setStatus(true);
        commonResponse.setMessage("Profile retrieved successfully.");
        commonResponse.setData(profile);

        logger.info("Profile fetched successfully for user with mobile: " + mobileNo);
        return new ResponseEntity<>(commonResponse, HttpStatus.OK);
    }
}