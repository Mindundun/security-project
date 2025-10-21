package com.example.security_project.exception;

public class CustomJWTException extends RuntimeException{
    
    public CustomJWTException(String msg){
        super(msg);
    }
    
}
