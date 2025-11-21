package com.jsdc.rfid.common.utils;


import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;

public class GenerateKey {

    public static byte[] getKey(){
        KeyGenerator kg;
        try {
            kg = KeyGenerator.getInstance("AES");
        } catch (NoSuchAlgorithmException e){
            String msg = "Unable to acquire AES algorithm. This is required to function";
            throw new IllegalStateException(msg, e);
        }
        kg.init(128);
        SecretKey sk = kg.generateKey();
        byte[] cipther = sk.getEncoded();
        return cipther;
    }
}
