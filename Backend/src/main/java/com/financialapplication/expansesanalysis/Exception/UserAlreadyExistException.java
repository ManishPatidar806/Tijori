package com.financialapplication.expansesanalysis.Exception;

public class UserAlreadyExistException extends Exception{
public UserAlreadyExistException(String message){
  super(message);
}

  public   UserAlreadyExistException(){
        super();
    }
}
