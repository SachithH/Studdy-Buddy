package com.learntv.studybuddy.support.validate;

import android.text.TextUtils;

public class Validating {
    public static boolean isValidPhoneNumber(String mobileNumber) {
        boolean digitsOnly = TextUtils.isDigitsOnly(mobileNumber);

        return digitsOnly && mobileNumber.length() == 10;

    }

    public static boolean isValidEmail(String emailString) {
        return !TextUtils.isEmpty(emailString) && android.util.Patterns.EMAIL_ADDRESS.matcher(emailString).matches();
    }
//   end email validate

    //    password validate
    public static boolean validatePwd(String passwordStr) {
        return 7 < passwordStr.length();
//        end password validate


    }
}
