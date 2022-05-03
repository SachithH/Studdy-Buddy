package com.learntv.studybuddy;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public class PasswordHashing {
    public static final String PBKDF2_ALGORITHM = "PBKDF2WithHmacSHA512";

    public static String createHash(String email,String password)
            throws NoSuchAlgorithmException, InvalidKeySpecException
    {
            return createHash(email,password.toCharArray());

    }


    private static String createHash(String email,char[] password)
            throws NoSuchAlgorithmException, InvalidKeySpecException
    {
        byte[] salt = email.getBytes();

        return pbkdf2(password,salt);
    }


    private static String pbkdf2(char[] password, byte[] salt)
            throws NoSuchAlgorithmException, InvalidKeySpecException
    {
        PBEKeySpec spec = new PBEKeySpec(password, salt, 100000, 512);
        SecretKeyFactory skf = SecretKeyFactory.getInstance(PBKDF2_ALGORITHM);
        byte[] result = skf.generateSecret(spec).getEncoded();
        return Base64.getEncoder().encodeToString(result);
    }
}
