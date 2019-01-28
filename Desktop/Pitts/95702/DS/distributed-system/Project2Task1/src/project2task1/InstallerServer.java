/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project2task1;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import static javax.xml.bind.DatatypeConverter.printHexBinary;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * installer server class
 *
 * @author apple
 */
public class InstallerServer {

    static String symmetricKey;
    static final Map<String, Integer> salts = new HashMap<>();
    static final Map<String, String> hashValues = new HashMap<>();
    static Map<String, String> sensors = new HashMap<>();

    //static initialization for the salts and hash values
    static {
        try {
            SecureRandom sr = new SecureRandom();
            int[] salt = new int[3];
            for (int i = 0; i < 3; i++) {
                salt[i] = sr.nextInt();
            }
            //store the salt for each installer
            salts.put("Barack", salt[0]);
            salts.put("Hillary", salt[1]);
            salts.put("Donald", salt[2]);
            String[] values = new String[3];
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest((salt[0] + "baracko" + "").getBytes(StandardCharsets.UTF_8));
            values[0] = printHexBinary(hash);
            hash = digest.digest((salt[1] + "hillaryc" + "").getBytes(StandardCharsets.UTF_8));
            values[1] = printHexBinary(hash);
            hash = digest.digest((salt[2] + "donaldt" + "").getBytes(StandardCharsets.UTF_8));
            values[2] = printHexBinary(hash);
            //store the user id as key and the hash value as value
            hashValues.put("Barack", values[0]);
            hashValues.put("Hillary", values[1]);
            hashValues.put("Donald", values[2]);
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(InstallerServer.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public static void main(String[] args) throws NoSuchAlgorithmException {

        System.out.println("Waiting for installers to visit...");
        try {
            //enter the symmetric key here
            System.out.println("Enter symmetric key as a 16-digit integer.");
            Scanner scanner = new Scanner(System.in);
            symmetricKey = scanner.nextLine();
            int serverPort = 7896; // the server port
            ServerSocket listenSocket = new ServerSocket(serverPort);

            while (true) {
                Socket clientSocket = listenSocket.accept();
                Connection c = new Connection(clientSocket);

            }
        } catch (IOException e) {
            System.out.println("Listen socket:" + e.getMessage());
        }

    }
}

/**
 * class for connection
 *
 * @author apple
 */
class Connection extends Thread {

    DataInputStream in;
    DataOutputStream out;
    Socket clientSocket;

    static int times = 1;

    /**
     * constructor for connection
     *
     * @param aClientSocket a socket passed
     * @throws NoSuchAlgorithmException exception thrown
     */
    public Connection(Socket aClientSocket) throws NoSuchAlgorithmException {
        try {
            clientSocket = aClientSocket;
            in = new DataInputStream(clientSocket.getInputStream());
            out = new DataOutputStream(clientSocket.getOutputStream());
            this.start();
        } catch (IOException e) {
            System.out.println("Connection:" + e.getMessage());
        }
    }

    /**
     * run method for the connection thread
     */
    public void run() {
        try {
            //times is the times that installer visits
            System.out.println();
            System.out.print("Get visit " + times);
            //for one connection, time plus 1
            times++;
            TEA tea = new TEA(InstallerServer.symmetricKey.getBytes());
            byte[] crypt = new byte[1024];
            int len = in.read(crypt);
            //use read method of the data inputstream to read data, return value is a integer
            byte[] re = new byte[len];
            //create a byte array to get the input data and cut the unused parts of the array
            System.arraycopy(crypt, 0, re, 0, len);
            byte[] result = tea.decrypt(re);
            String s = new String(result);
            //if condition for judging whether the symmetric value entered by the client is correct
            //if not , then close the socket
            if (!s.matches("\\A\\p{ASCII}*\\z")) {
                System.out.println();
                System.out.println("Illegal symmetric key used. This may be an attack.");
                in.close();
                out.close();
                clientSocket.close();
            } else {
                //creating json object for passing parameters in the json string
                JSONObject obj = new JSONObject(s);
                Object credentials = obj.get("Credentials");
                JSONObject jObject = new JSONObject(credentials.toString());
                String userID = (String) jObject.get("ID");
                String password = (String) jObject.get("passwd");
                String title = "";
                if (userID.equals("Barack")) {
                    title = "Chief Sensor Installer";
                } else {
                    title = "Associate Sensor Installer";
                }
                //print out the installer id and title
                System.out.print(" from " + userID + "," + title);

                byte[] original = "Illegal ID or Password".getBytes();
                //if the salts map doesn't contain the installer id, then pass the illegal information
                if (!InstallerServer.salts.containsKey(userID)) {
                    out.write(tea.encrypt(original));
                } else {
                    //val is the salt for each installer
                    int val = InstallerServer.salts.get(userID);
                    //using SHA-256 to hash the salt plus password
                    MessageDigest digest = MessageDigest.getInstance("SHA-256");
                    byte[] hash = digest.digest((val + password + "").getBytes(StandardCharsets.UTF_8));
                    //if they can match, then the authentification is passed
                    if (!printHexBinary(hash).equals(InstallerServer.hashValues.get(userID))) {
                        out.write(tea.encrypt(original));
                        System.out.println();
                        System.out.println("Illegal Password attempt. This may be an attack.");
                    } else {
                        String id = obj.get("Sensor ID").toString();
                        String latitude = obj.get("Latitude").toString();
                        String longitude = (String) obj.get("Longitude").toString();
                        String location = longitude + "," + latitude + "," + "0.00000";
                        //if the sensor already exists in the map, then moved it to the updated location
                        if (InstallerServer.sensors.containsKey(id)) {
                            if(userID.equals("Barack")){
                                InstallerServer.sensors.put(id, userID + "," + location);
                            System.out.println(", a sensor has been moved");
                            out.write(tea.encrypt("Thank you. The sensor’s new location was securely transmitted to GunshotSensing Inc.".getBytes()));
                            }else{
                            out.write(tea.encrypt("Not authorized to move.".getBytes()));
                            }
                            
                        } else {
                            //otherwise, add the new sensor
                            TEA tea2 = new TEA(InstallerServer.symmetricKey.getBytes());
                            InstallerServer.sensors.put(id, userID + "," + location);
                            byte[] output = tea2.encrypt("Thank you. The sensor’s location was securely transmitted to GunshotSensing Inc.".getBytes());
                            out.write(output);
                            out.flush();
                        }
                    }
                    //creating a kml file
                    toKML();
                }
            }
        } catch (EOFException e) {
            System.out.println("EOF:" + e.getMessage());
        } catch (IOException e) {
            System.out.println("readline:" + e.getMessage());
        } catch (JSONException ex) {
            Logger.getLogger(Connection.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(Connection.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {/*close failed*/
            }
        }

    }

    /**
     * method for generating kml file
     *
     * @param installer string of the installer name
     * @throws FileNotFoundException exception thrown
     * @throws UnsupportedEncodingException exception thrown
     */
    public void toKML() throws FileNotFoundException, UnsupportedEncodingException {
        String kml = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n"
                + "<kml xmlns=\"http://earth.google.com/kml/2.2\">\n"
                + "<Document>\n"
                + "  <Style id=\"style1\">\n"
                + "  <IconStyle>\n"
                + "    <Icon>\n"
                + "      <href>https://lh3.googleusercontent.com/MSOuW3ZjC7uflJAMst-cykSOEOwI_cVz96s2rtWTN4-Vu1NOBw80pTqrTe06R_AMfxS2=w170 \n"
                + "</href>\n"
                + "    </Icon>\n"
                + "   </IconStyle>\n"
                + "   </Style>\n";
        //for each sensor object, giving the parameters inside the placemark tag
        for (Map.Entry<String, String> entry : InstallerServer.sensors.entrySet()) {
            String microphone = "Microphone " + Integer.parseInt(entry.getKey());

            kml += "<Placemark>\n"
                    + "<name>" + microphone + "</name>\n"
                    + "<description>" + entry.getValue().split(",")[0] + "</description>\n"
                    + "<styleUrl>#style1</styleUrl>\n"
                    + "<Point>\n"
                    + "<coordinates>" + entry.getValue().split(",")[1] + "," + entry.getValue().split(",")[2] + ",0.00</coordinates>\n"
                    + "</Point>\n"
                    + "</Placemark>\n";
        }
        kml += "</Document>\n"
                + "</kml>\n";
        String deskTopLocation = System.getProperty("user.home") + "/Desktop/Sensors.kml";
        PrintWriter writer = new PrintWriter(deskTopLocation, "UTF-8");
        writer.println(kml);
        writer.close();

    }
}
