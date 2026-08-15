/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package app.security;

import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

/**
 *
 * @author PC
 */
public class PasswordHash {
    public static final String PBKDF2_ALGORITHM = "PBKDF2WithHmacSHA256";
    public static final int SALT_BYTES = 16;
    public static final int HASH_BYTES = 32;
    public static final int PBKDF2_ITERATIONS = 600000;
    
    public static final int ITEARTION_INDEX = 0;
    public static final int SALT_INDEX = 1;
    public static final int PBKDF2_INDEX = 2;
    
    public static String createHash(String password) throws NoSuchAlgorithmException, InvalidKeySpecException{
        if(password == null || password.isBlank())
            throw new IllegalArgumentException("Lozinka ne sme biti prazna");
        return createHash(password.toCharArray());
    }
    
    public static String createHash(char[] password){
        try{
            SecureRandom random = new SecureRandom();
            byte[] salt = new byte[SALT_BYTES];
            random.nextBytes(salt);

            byte[] hash = pbkdf2(password, salt, PBKDF2_ITERATIONS, HASH_BYTES);

            return PBKDF2_ITERATIONS + ":" + toHex(salt) + ":" + toHex(hash);
        }catch(NoSuchAlgorithmException | InvalidKeySpecException e){
            throw new RuntimeException("Greska pri generisanju hash-a lozinke");
        }
    }
    
    
    public static boolean validatePassword(String password, String goodHash) throws NoSuchAlgorithmException, InvalidKeySpecException{
        if(password == null || goodHash == null || goodHash.isBlank())
            return false;
        return validatePassword(password.toCharArray(), goodHash);
    }
    
    public static boolean validatePassword(char[] password, String goodHash){
        try{
            String[] params = goodHash.split(":");
            int iterations = Integer.parseInt(params[ITEARTION_INDEX]);
            byte[] salt = fromHex(params[SALT_INDEX]);
            byte[] hash = fromHex(params[PBKDF2_INDEX]);

            byte[] testHash = pbkdf2(password, salt, iterations, hash.length);

            return slowEquals(hash, testHash);
        }catch(Exception e){
            return false;
        }

    }
    
    private static boolean slowEquals(byte[] a, byte[] b){
        int diff = a.length ^ b.length;
        for(int i = 0; i < a.length && i < b.length; i++){
            diff |= a[i] ^ b[i];
        }
        return diff == 0;
    }
    
    public static byte[] pbkdf2(char[] password, byte[] salt, int iterations, int bytes) throws NoSuchAlgorithmException, InvalidKeySpecException{
        PBEKeySpec spec = new PBEKeySpec(password, salt, iterations, bytes*8);
        SecretKeyFactory skf = SecretKeyFactory.getInstance(PBKDF2_ALGORITHM);
        return skf.generateSecret(spec).getEncoded();
    }
    
    private static byte[] fromHex(String hex){
        byte[] binary = new byte[hex.length() / 2];
        for(int i = 0; i < binary.length; i++){
            binary[i] = (byte)Integer.parseInt(hex.substring(2*i, 2*i+2), 16);
        }
        return binary;
    }
    
    private static String toHex(byte[] array){
        BigInteger bi = new BigInteger(1, array);
        String hex = bi.toString(16);
        int paddingLength = (array.length * 2) - hex.length();
        if(paddingLength > 0)
            return String.format("%0" + paddingLength + "d", 0) + hex;
        else
            return hex;
    }
}
