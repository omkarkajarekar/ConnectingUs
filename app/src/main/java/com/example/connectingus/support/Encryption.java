package com.example.connectingus.support;

import com.scottyab.aescrypt.AESCrypt;

import java.security.GeneralSecurityException;

public class Encryption {
    public static final String PASSWORD = "1123581321345589";
    public String encrypt(String plaintext){
        String encrypted = "1";
        try {
            encrypted = AESCrypt.encrypt(PASSWORD,plaintext);
        }catch (GeneralSecurityException e){
            e.printStackTrace();
        }
        return encrypted;
    }
    public String decrypt(String ciphertext){
        String decrypted = "1";
        try {
            decrypted = AESCrypt.decrypt(PASSWORD,ciphertext);
        }catch (GeneralSecurityException e){
            e.printStackTrace();
        }
        return decrypted;
    }
}
