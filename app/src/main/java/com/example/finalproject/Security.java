package com.example.finalproject;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;

/* Has a method to generate "securedPassword"
from "originalPassword" and "salt" using "sha-512"
and a method to generate "salt" byte[] */

public class Security {
    public static String getSecuredPassword(String originalPassword, byte[] salt) {
        String securedPassword = null;

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

    // Uses SecureRandom to generate random bytes to byte[] "salt", returns salt
    public static byte[] getSalt() {
        byte[] salt = new byte[16];
        try {

            SecureRandom sr = SecureRandom.getInstance("SHA1PRNG", "SUN");
            sr.nextBytes(salt);
        } catch (NoSuchAlgorithmException | NoSuchProviderException e) {
            e.printStackTrace();
        }
        return salt;
    }
}