package com.learntv.studybuddy.support;

public class ShowErrors {
    String errors;
    public String Errors(String errorCode) {
        switch (errorCode) {
            case "1000":
                errors = "Internal server error. Please try again Later";
                break;
            case "1001":
                errors = "Username or Password incorrect";
                break;
            case "1002":
                errors = "Please enter email correct and try again";
                break;
            case "1003":
                errors = "Account not activated";
                break;
            case "1004":
                errors = "Login expired. Please Sign In";
                break;
            case "1005":
                errors = "Token of session is not provided";
                break;
            case "1006":
                errors = "Parameter missing";
                break;
            case "1007":
                errors = "Mobile number required";
                break;
            case "1008":
                errors = "User has been already registered. But activation has not been verified.";
                break;
            case "1009":
                errors = "Registration rejected, Email/Mobile found";
                break;
            case "1010":
                errors = "Parameters not match.";
                break;
            case "1205":
                errors = "OTP verification is required.";
                break;
            default:
                errors = "Something went wrong. Please try again later";
        }
        return errors;
    }

    public String Errors(String errorCode,String description) {
        switch (errorCode) {
            case "1000":
                errors = "Internal server error. Please try again Later";
                break;
            case "1001":
                errors = "Username or Password incorrect";
                break;
            case "1002":
                errors = "Please enter email correct and try again";
                break;
            case "1003":
                errors = "Account not activated";
                break;
            case "1004":
                errors = "Login expired. Please Sign In";
                break;
            case "1005":
                errors = "Token of session is not provided";
                break;
            case "1006":
                errors = "Parameter missing";
                break;
            case "1007":
                errors = "Mobile number required";
                break;
            case "1008":
                errors = "User has been already registered. But activation has not been verified.";
                break;
            case "1009":
                errors = "Registration rejected, Email/Mobile found";
                break;
            case "1010":
                errors = "Parameters not match.";
                break;
            case "1205":
                errors = "OTP verification is required.";
                break;
            default:
                errors = description;
        }
        return errors;
    }
}
