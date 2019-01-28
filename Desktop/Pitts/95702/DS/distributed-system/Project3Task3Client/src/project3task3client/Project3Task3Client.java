/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project3task3client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;

/**
 *
 * @author apple
 */
// A simple class to wrap a result.
class Result {

    String value;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}

public class Project3Task3Client {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws NoSuchAlgorithmException {
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
                    BigInteger d = new BigInteger("339177647280468990599683753475404338964037287357290649639740920420195763493261892674937712727426153831055473238029100340967145378283022484846784794546119352371446685199413453480215164979267671668216248690393620864946715883011485526549108913");
                    BigInteger n = new BigInteger("2688520255179015026237478731436571621031218154515572968727588377065598663770912513333018006654248650656250913110874836607777966867106290192618336660849980956399732967369976281500270286450313199586861977623503348237855579434471251977653662553");
                    MessageDigest digest = MessageDigest.getInstance("SHA-256");
                    byte[] hash = digest.digest(transaction.getBytes(StandardCharsets.UTF_8));
                    byte[] dest = new byte[hash.length + 1];
                    System.arraycopy(hash, 0, dest, 1, hash.length);
                    dest[0] = 0;
                    BigInteger signature = new BigInteger(dest);
                    BigInteger encoded = signature.modPow(d, n);
                    String result = addBlock(transaction + "#" + encoded.toString(), level);
                    System.out.println(result);
                    break;
                case 2:
                    System.out.println("Verifying entire chain");
                    long t1 = System.currentTimeMillis();
                    verify();
                    long t2 = System.currentTimeMillis();
                    System.out.println("The verification progress takes " + (t2 - t1) + " milliseconds");
                    break;
                case 3:
                    System.out.println("View the blockchain");
                    view();
                    break;
                case 4:
                    break;

            }

        }

    }
    /**
     * view method
     * @return return the result
     */
    public static String view() {
        Result r = new Result();
        int status = 0;
        if ((status = doGet("view", r)) != 200) {
            return "Error from server " + status;
        }
        return r.getValue();
    }
    /**
     * verify method
     * @return return the boolean value
     */
    public static boolean verify() {
        Result r = new Result();
        return doGet("verify", r) == 200;
    }
    /**
     * add block method
     * @param transaction string value
     * @param level difficulty level
     * @return return the result
     */
    public static String addBlock(String transaction, int level) {
        Result r = new Result();
        int status = 0;
        if ((status = doPost(transaction, level, r)) != 200) {
            return "Error from server " + status;
        }
        return r.getValue();
    }
    /**
     * do get method 
     * @param name operation name
     * @param r result method
     * @return return status
     */
    public static int doGet(String name, Result r) {
        // Make an HTTP GET passing the name on the URL line
        r.setValue("");
        String response = "";
        HttpURLConnection conn;
        int status = 0;
        try {
            // pass the name on the URL line
            URL url = new URL("http://localhost:8080/Project3Task3Server/Project3Task3Servlet" + "//" + name);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            // tell the server what format we want back
            conn.setRequestProperty("Accept", "text/plain");
            
            // wait for response
            status = conn.getResponseCode();

            // If things went poorly, don't try to read any response, just return.
            if (status != 200) {
                // not using msg
                String msg = conn.getResponseMessage();
                return conn.getResponseCode();
            }
            String output = "";
            // things went well so let's read the response
            BufferedReader br = new BufferedReader(new InputStreamReader(
                    (conn.getInputStream())));

            while ((output = br.readLine()) != null) {
                response += output;

            }
            System.out.println(response);
            conn.disconnect();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // return value from server 
        // set the response object
        r.setValue(response);
        // return HTTP status to caller
        return status;
    }
    /**
     * do post method for dealing with add block operation
     * @param transaction transaction data
     * @param difficulty difficulty level
     * @param r result object
     * @return return status
     */
    public static int doPost(String transaction, int difficulty, Result r) {

        int status = 0;
        String output;
        String response = "";

        try {
            // Make call to a particular URL
            URL url = new URL("http://localhost:8080/Project3Task3Server/Project3Task3Servlet/");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            // set request method to POST and send name value pair
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            // write to POST data area
            OutputStreamWriter out = new OutputStreamWriter(conn.getOutputStream());
            out.write(transaction + "," + difficulty);
            out.close();
            BufferedReader br = new BufferedReader(new InputStreamReader(
                    (conn.getInputStream())));

            while ((output = br.readLine()) != null) {

                response += output;

            }
            // get HTTP response code sent by server
            status = conn.getResponseCode();
            //close the connection
            conn.disconnect();
        } // handle exceptions
        catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // return HTTP status
        r.setValue(response);
        return status;
    }

}
