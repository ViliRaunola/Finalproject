package com.example.finalproject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// Generates "securedPassword" using SHA-512 and salt from user's eMail
public class Security {
    public static String getSecuredPassword(String originalPassword, String eMail) {
        String securedPassword = null;
        // Creates "salt" byte[] from username
        byte[] salt = eMail.getBytes();
        try {
            // Creates MessageDigest instance for "SHA-512"
            MessageDigest md = MessageDigest.getInstance("SHA-512");

            // Adds salt bytes to "md"
            md.update(salt);
            byte[] bytes = md.digest(originalPassword.getBytes());

            // Converts byte[] to string "securedPassword" in hexadecimal using StringBuilder()
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < bytes.length; i++) {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            securedPassword = sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return securedPassword;
    }
    //https://stackoverflow.com/questions/1795402/check-if-a-string-contains-a-special-character 3rd answer Pir Fahim Shah
    public static boolean passwordChecker(String password) {
        if ((password.length() > 11)) {
            Pattern capitalLetter = Pattern.compile("[A-Z]");
            Pattern smallLetter = Pattern.compile("[a-z]");
            Pattern digit = Pattern.compile("[0-9]");
            Pattern special = Pattern.compile("[!@#$%&*()_+=|<>?{}\\[\\]~-]");

            Matcher hasSmallLetter = smallLetter.matcher(password);
            Matcher hasCapitalLetter = capitalLetter.matcher(password);
            Matcher hasDigit = digit.matcher(password);
            Matcher hasSpecial = special.matcher(password);

            return hasSmallLetter.find() && hasDigit.find() && hasSpecial.find() && hasCapitalLetter.find();
        } else{
          return false;
        }
    }
}