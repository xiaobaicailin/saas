package com.lzh.web.exceptions;

public class CustomeException extends Exception {
    private String message;

    public CustomeException(String message){
        this.message = message;
    }

    public  String getMessage(){
        return message;
    }
}
