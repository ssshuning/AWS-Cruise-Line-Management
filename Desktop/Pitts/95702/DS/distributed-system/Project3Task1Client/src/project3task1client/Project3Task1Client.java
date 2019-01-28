/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project3task1client;

import edu.cmu.andrew.shuningc.NoSuchAlgorithmException_Exception;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;

/**
 * client class
 * @author apple
 */
public class Project3Task1Client {

    /** 
     * main method
     * @param args the command line arguments
     * @throws java.security.NoSuchAlgorithmException exception thrown
     * @throws edu.cmu.andrew.shuningc.NoSuchAlgorithmException_Exception exception thrown
     */
    public static void main(String[] args) throws NoSuchAlgorithmException, NoSuchAlgorithmException_Exception {
        Scanner scanner = new Scanner(System.in);
        int selection = -1;
        //the option give
        while (selection != 4) {
            System.out.println("1.Add a transaction to the blockchain.");
            System.out.println("2.Verify the blockchain.");
            System.out.println("3.View the blockchain.");
            System.out.println("4.Exit.");
            selection = scanner.nextInt();
            scanner.nextLine();
            switch (selection) {
                case 1:
                    System.out.println("Enter difficulty > 0");
                    int level = scanner.nextInt();
                    scanner.nextLine();
                    System.out.println("Enter transaction");
                    String transaction = scanner.nextLine();
                    //public key
                    BigInteger d = new BigInteger("339177647280468990599683753475404338964037287357290649639740920420195763493261892674937712727426153831055473238029100340967145378283022484846784794546119352371446685199413453480215164979267671668216248690393620864946715883011485526549108913");                    
                    BigInteger n = new BigInteger("2688520255179015026237478731436571621031218154515572968727588377065598663770912513333018006654248650656250913110874836607777966867106290192618336660849980956399732967369976281500270286450313199586861977623503348237855579434471251977653662553");                    
                    MessageDigest digest = MessageDigest.getInstance("SHA-256");
                    byte[] hash = digest.digest(transaction.getBytes(StandardCharsets.UTF_8));
                    byte[] dest = new byte[hash.length + 1];
                    //copy to the new array
                    System.arraycopy(hash, 0, dest, 1, hash.length);
                    dest[0] = 0;
                    //get the big integer
                    BigInteger signature = new BigInteger(dest);
                    //encryption
                    BigInteger encoded = signature.modPow(d,n);
                    addBlock(transaction + "#" + encoded.toString(), level);
                    break;
                case 2:
                    System.out.println("Verifying entire chain");
                    long t1 = System.currentTimeMillis();
                    verify();
                    long t2 = System.currentTimeMillis();
                    System.out.println("Total execution time required to verify was " + (t2 - t1) + " milliseconds");
                    break;
                case 3:
                    System.out.println("View the blockchain");
                    view();
                    break;
                case 4:
                    break;

            }

        }

        // TODO code application logic here
    }

   
    //the verify method for client
    private static boolean verify() throws NoSuchAlgorithmException_Exception {
        edu.cmu.andrew.shuningc.Project3Task1Service_Service service = new edu.cmu.andrew.shuningc.Project3Task1Service_Service();
        edu.cmu.andrew.shuningc.Project3Task1Service port = service.getProject3Task1ServicePort();
        return port.verify();
    }
    //the view method for client
    private static String view() throws NoSuchAlgorithmException_Exception {
        edu.cmu.andrew.shuningc.Project3Task1Service_Service service = new edu.cmu.andrew.shuningc.Project3Task1Service_Service();
        edu.cmu.andrew.shuningc.Project3Task1Service port = service.getProject3Task1ServicePort();
        String message = port.view();
        System.out.println(message);
        return message;
    }
    //the add block method for client
    private static String addBlock(java.lang.String arg0, int arg1) throws NoSuchAlgorithmException_Exception {
        edu.cmu.andrew.shuningc.Project3Task1Service_Service service = new edu.cmu.andrew.shuningc.Project3Task1Service_Service();
        edu.cmu.andrew.shuningc.Project3Task1Service port = service.getProject3Task1ServicePort();
        String message = port.addBlock(arg0, arg1);
        System.out.println(message);
        return message;
    }

    

}
