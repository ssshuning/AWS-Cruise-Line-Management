/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project4task1client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.NoSuchAlgorithmException;

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

public class Project4Task1Client {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws NoSuchAlgorithmException {
        System.out.println(search());
    }
    /**
     * view method
     * @return return the result
     */
    public static String search() {
        Result r = new Result();
        int status = 0;
        if ((status = doGet("Taylor Swift", r)) != 200) {
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
            URL url = new URL("http://localhost:8080/Project4Task1/Project4Servlet/" + name);
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
    
}
