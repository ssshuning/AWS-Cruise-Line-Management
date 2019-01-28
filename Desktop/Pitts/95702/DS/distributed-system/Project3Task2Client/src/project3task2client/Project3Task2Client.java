/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project3task2client;

import cmu.edu.andrew.shuningc.IOException_Exception;
import cmu.edu.andrew.shuningc.NoSuchAlgorithmException_Exception;
import cmu.edu.andrew.shuningc.ParserConfigurationException_Exception;
import cmu.edu.andrew.shuningc.SAXException_Exception;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;

/**
 * client class
 * @author apple
 */
public class Project3Task2Client {

    /** 
     * main method
     * @param args the command line arguments
     */
    public static void main(String[] args) throws NoSuchAlgorithmException, SAXException_Exception, ParserConfigurationException_Exception, IOException_Exception, NoSuchAlgorithmException_Exception {
        // TODO code application logic here
        Scanner scanner = new Scanner(System.in);
        int selection = -1;
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
                    //copy the byte arry
                    System.arraycopy(hash, 0, dest, 1, hash.length);
                    dest[0] = 0;
                    BigInteger signature = new BigInteger(dest);
                    //encryption
                    BigInteger encoded = signature.modPow(d, n);
                    //xml message carrying add block operation
                    String xmlMessage
                            = "<data>"
                            + "<operation>"
                            + "   <name>addBlock</name>"
                            + "   <transaction>" + transaction + "#" + encoded.toString() + "</transaction>"
                            + "   <difficulty>" + level + "</difficulty>"
                            + " </operation>"
                            + "</data>";
                    dealMessage(xmlMessage);
                    break;
                case 2:
                    //xml message carrying verify operation
                    System.out.println("Verifying entire chain");
                    xmlMessage
                            = "<data>"
                            + "<operation>"
                            + "   <name>verify</name>"
                            + " </operation>"
                            + "</data>";
                    long t1 = System.currentTimeMillis();
                    dealMessage(xmlMessage);
                    long t2 = System.currentTimeMillis();
                    System.out.println("Total execution time required to verify was " + (t2 - t1) + " milliseconds");
                    break;
                case 3:
                    //xml message carrying view operation
                    xmlMessage
                            = "<data>"
                            + "<operation>"
                            + " <name>view</name>"
                            + " </operation>"
                            + " </data>";
                    System.out.println("View the blockchain");
                    dealMessage(xmlMessage);
                    break;
                case 4:
                    break;

            }

        }
    }
    /**
     * the deal message method combining three operations
     * @param arg0
     * @return return the message got from the server
     * @throws NoSuchAlgorithmException_Exception
     * @throws ParserConfigurationException_Exception
     * @throws IOException_Exception
     * @throws SAXException_Exception 
     */
    private static String dealMessage(java.lang.String arg0) throws NoSuchAlgorithmException_Exception, ParserConfigurationException_Exception, IOException_Exception, SAXException_Exception {
        cmu.edu.andrew.shuningc.Project3Task2Service_Service service = new cmu.edu.andrew.shuningc.Project3Task2Service_Service();
        cmu.edu.andrew.shuningc.Project3Task2Service port = service.getProject3Task2ServicePort();
        String message = port.dealMessage(arg0);
        if(message!=null){
            System.out.println(message);
        }
        return message;
    }

    

}
