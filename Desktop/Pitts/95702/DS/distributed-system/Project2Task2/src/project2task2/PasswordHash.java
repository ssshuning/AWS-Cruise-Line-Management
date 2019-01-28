/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project2task2;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Scanner;
import static javax.xml.bind.DatatypeConverter.printHexBinary;

/**
 * class for hashing password
 * @author apple
 */
public class PasswordHash {
    /**
     * main method
     * @param args command line parameter
     * @throws NoSuchAlgorithmException exception thrown
     */
    public static void main(String[] args) throws NoSuchAlgorithmException {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter user ID:");
        String id = scanner.nextLine();
        System.out.println();
        System.out.print("Enter password:");
        String password = scanner.nextLine();
        System.out.println();
        System.out.println("Generating a random number for salt using SecureRandom");
        System.out.println("User ID="+id);
        //generating a random salt using secure random object
        SecureRandom sr = new SecureRandom();
        int salt = sr.nextInt();
        System.out.println("Salt="+salt);
        //hash the value of salt plus password using sha-256 algorithm
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest((salt+password+"").getBytes(StandardCharsets.UTF_8));
        System.out.println("Hash of salt + password ="+printHexBinary(hash));
        System.out.println("Enter user ID for authentication testing");
        String idTest = scanner.nextLine();
        System.out.println();
        System.out.println("Enter password for authentication testing");
        String passwordTest = scanner.nextLine();
        System.out.println();
        //if condition for judging the pair of password and user
        if(idTest.equals(id)&&passwordTest.equals(password)){
            System.out.println("Validated user id and password pair");
        }else{
            System.out.println("Not able to validate this user id, password pair.");
        }        
    }
}
