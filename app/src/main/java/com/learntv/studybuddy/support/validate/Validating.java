package com.learntv.studybuddy.support.validate;

import android.text.TextUtils;

public class Validating {
    public static boolean validateEmail(String emailString) {

        if (emailString.isEmpty() || !isValidEmail(emailString)){
            return false;
        }else {
            return true;
        }
    }

    public static boolean isValidEmail(String emailString) {
        return !TextUtils.isEmpty(emailString) && android.util.Patterns.EMAIL_ADDRESS.matcher(emailString).matches();
    }
//   end email validate

    //    password validate
    public static boolean validatePwd(String passwordStr) {
        if (0 < passwordStr.length()) {
            return true;
        }else {
            return false;
        }
//        end password validate


    }
}
