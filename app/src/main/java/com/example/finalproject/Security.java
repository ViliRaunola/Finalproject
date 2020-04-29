package com.example.finalproject;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

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
}