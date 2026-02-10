package com.financialapplication.tijori.Service;

import com.financialapplication.tijori.Exception.BusinessException;
import com.financialapplication.tijori.Exception.UserAlreadyExistException;
import com.financialapplication.tijori.Exception.NotFoundException;
import com.financialapplication.tijori.Model.DTO.UserProfileDto;
import com.financialapplication.tijori.Model.Entity.User;
import com.financialapplication.tijori.Model.Request.RegisterRequest;

/**
 * Service interface for authentication and user management.
 */
public interface AuthService {

    User signUpUser(RegisterRequest registerRequest) throws NotFoundException, UserAlreadyExistException;

    User loginUser(String mobile) throws BusinessException, NotFoundException;

    UserProfileDto getProfile(String mobileNo) throws NotFoundException;
}
