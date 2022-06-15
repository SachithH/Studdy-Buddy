package com.learntv.studybuddy.retrofit;


public class SignInResponse {
    private String success;
    private String token;
    private String message;
    private int errorcode;

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getErrorcode() { return errorcode; }

    public void setErrorcode(int errorcode) { this.errorcode = errorcode; }
}