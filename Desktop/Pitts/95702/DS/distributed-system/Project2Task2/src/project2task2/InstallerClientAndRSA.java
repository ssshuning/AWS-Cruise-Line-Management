/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project2task2;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.math.BigInteger;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Random;
import java.util.Scanner;

/**
 * class for client and RSA
 * @author apple
 */
public class InstallerClientAndRSA {

    public static void main(String[] args) {

        Socket s = null;
        try {
            int serverPort = 7896;
            s = new Socket("localhost", serverPort);
            Scanner scanner = new Scanner(System.in);
            System.out.print("Enter your ID:");
            String id = scanner.nextLine();
            System.out.println();
            System.out.print("Enter your password:");
            String password = scanner.nextLine();
            System.out.println();
            System.out.print("Enter sensor ID:");
            String sensorID = scanner.nextLine();
            System.out.println();
            System.out.print("Enter new sensor location:");
            String location = scanner.nextLine();
            System.out.println();
            Random rnd = new Random();
            BigInteger key = new BigInteger(16 * 8, rnd);
            DataInputStream in = new DataInputStream(s.getInputStream());
            DataOutputStream out = new DataOutputStream(s.getOutputStream());
            String message = "{ \"Credentials\" : { \"ID\":" + "\"" + id + "\"" + ", \"passwd\": " + "\"" + password + "\"" + "},\"Sensor ID\":" + sensorID
                    + ",\"Latitude\":" + location.split(",")[1] + ",\"Longitude\":" + location.split(",")[0] + "}";
            
            TEA tea = new TEA(key.toByteArray());
            //public key
            BigInteger e = new BigInteger("65537");
            //n given
            BigInteger n = new BigInteger("4761111703482184813412954676217733168238323022382458343082984851923042832458907495022342481840835679607756731534493009676568475653378644433744042004009164930988299510788861535990526654031886884025703238485640863252747380390936713651698316147");
            //encrypted key
            BigInteger c = key.modPow(e, n);
            
            out.write(c.toByteArray());//the key sent to the server
            byte[] crypt = tea.encrypt(message.getBytes());//the message sent to the server

            out.write(crypt);
            byte[] output = new byte[1024];
            int len = in.read(output);
            //nothing received
            if (len == -1) {
                in.close();
                out.close();
                s.close();
            }
            byte[] re = new byte[len];
            //get the message transmitted from the server
            System.arraycopy(output, 0, re, 0, len);
            byte[] result = tea.decrypt(re);
            System.out.println(new String(result));
        } catch (UnknownHostException e) {
            System.out.println("Socket:" + e.getMessage());
        } catch (EOFException e) {
            System.out.println("EOF:" + e.getMessage());
        } catch (IOException e) {
            System.out.println("readline:" + e.getMessage());
        } finally {
            if (s != null) {
                try {
                    s.close();
                } catch (IOException e) {
                    System.out.println("close:" + e.getMessage());
                }
            }
        }
    }

   

}
