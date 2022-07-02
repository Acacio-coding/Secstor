package com.ifsc.secstor.api.model;

public class UserErrorModel extends ErrorModel{
    public UserErrorModel(int status, String title, String message, String path) {
        super(status, title, message, path);
    }
}
