/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project2task1;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

/**
 * installer client class
 * @author apple
 */
public class InstallerClient {
    /**
     * main method 
     * @param args command line array
     */
    public static void main(String[] args) {

        Socket s = null;
        try {
            int serverPort = 7896;
            s = new Socket("localhost", serverPort);
            System.out.println("Enter symmetric key as a 16-digit integer.");
            Scanner scanner = new Scanner(System.in);
            String symmetricKey = scanner.nextLine();
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
            //input,output stream for the data
            DataInputStream in = new DataInputStream(s.getInputStream());
            DataOutputStream out = new DataOutputStream(s.getOutputStream());
            //json string passed before encryption
            String message = "{ \"Credentials\" : { \"ID\":" + "\""+id +"\""+ ", \"passwd\": " +"\""+ password +"\""+ "},\"Sensor ID\":" + sensorID
                    + ",\"Latitude\":" + location.split(",")[1] + ",\"Longitude\":" + location.split(",")[0] + "}";
            TEA tea = new TEA(symmetricKey.getBytes());
            byte[] original = message.getBytes();
            byte[] crypt = tea.encrypt(original);
           //write data to the server side            
            out.write(crypt);
           
            byte[] output = new byte[1024];
            int len = in.read(output);
            
            if(len==-1){
                in.close();
                out.close();
                s.close();
            }
            byte[] re = new byte[len];
            //accept the data passed from the server side
            System.arraycopy(output,0,re,0,len);
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
