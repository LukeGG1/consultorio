/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package clases;

/**
 *
 * @author luism
 */
import at.favre.lib.crypto.bcrypt.BCrypt;

public class HashPassword {

    public static String hash (String password){
        return BCrypt.withDefaults().hashToString(12, password.toCharArray());
    }
    
    public static boolean verify(String password, String hashedPassword){
        BCrypt.Result result = BCrypt.verifyer().verify(password.toCharArray(), hashedPassword);
        return result.verified;
    }
}