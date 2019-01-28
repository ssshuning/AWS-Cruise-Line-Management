/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project2task2;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import static javax.xml.bind.DatatypeConverter.printHexBinary;

/**
 * class for RSAServer class
 * @author apple
 */
public class InstallerServerAndRSA {

    static BigInteger privateKey;
    static BigInteger n;
    static final Map<String, Integer> salts = new HashMap<>();
    static final Map<String, String> hashValues = new HashMap<>();
    static Map<Integer, String> sensors = new HashMap<>();

    static {
        try {
            //n and privated key
            n = new BigInteger("4761111703482184813412954676217733168238323022382458343082984851923042832458907495022342481840835679607756731534493009676568475653378644433744042004009164930988299510788861535990526654031886884025703238485640863252747380390936713651698316147");
            privateKey = new BigInteger("3390031837455066178389339106321990639832050681545616005029592522241434162898247697449114081703781927052143981572631975881459582843801422876309748822637910734885312757222802872486865161466908537754594946956429579927695110900797601201717864833");
            SecureRandom sr = new SecureRandom();
            int[] salt = new int[3];
            //generate the salt for each installer
            for (int i = 0; i < 3; i++) {
                salt[i] = sr.nextInt();
            }
            salts.put("Barack", salt[0]);
            salts.put("Hillary", salt[1]);
            salts.put("Donald", salt[2]);
            String[] values = new String[3];
            //Using SHA-256 to hash the salt plus password
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest((salt[0] + "baracko" + "").getBytes(StandardCharsets.UTF_8));
            values[0] = printHexBinary(hash);
            hash = digest.digest((salt[1] + "hillaryc" + "").getBytes(StandardCharsets.UTF_8));
            values[1] = printHexBinary(hash);
            hash = digest.digest((salt[2] + "donaldt" + "").getBytes(StandardCharsets.UTF_8));
            values[2] = printHexBinary(hash);
            hashValues.put("Barack", values[0]);
            hashValues.put("Hillary", values[1]);
            hashValues.put("Donald", values[2]);
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(InstallerServerAndRSA.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public static void main(String[] args) throws NoSuchAlgorithmException {

        System.out.println("Waiting for installers to visit...");
        try {
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
 * connection class
 * @author apple
 */
class Connection extends Thread {

    DataInputStream in;
    DataOutputStream out;
    Socket clientSocket;

    static int times = 1;
    /**
     * constructor
     * @param aClientSocket socket parameter passed
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
     * run method for the thread
     */
    public void run() {
        try {
            System.out.println();
            System.out.print("Got visit " + times);
            times++;
            byte[] tmp = new byte[1024];
            int len1 = in.read(tmp);
            byte[] re1 = new byte[len1];
            //create a byte array to get the input data and cut the unused parts of the array
            System.arraycopy(tmp, 0, re1, 0, len1);
            BigInteger c = new BigInteger(re1);
            BigInteger key = c.modPow(InstallerServerAndRSA.privateKey, InstallerServerAndRSA.n);
            //private key for generating a tea object
            TEA tea = new TEA(key.toByteArray());
            byte[] crypt = new byte[1024];
            int len = in.read(crypt);
            //use read method of the data inputstream to read data, return value is a integer
            byte[] re = new byte[len];
            //create a byte array to get the input data and cut the unused parts of the array
            System.arraycopy(crypt, 0, re, 0, len);
            byte[] result = tea.decrypt(re);
            String s = new String(result);
            //use regex to judge whether the string is in ASCII,otherwise, the public key is not valid
            if (!s.matches("\\A\\p{ASCII}*\\z")) {
                System.out.println();
                System.out.println("Illegal public key used. This may be an attack.");
                in.close();
                out.close();
                //then close the client socket
                clientSocket.close();
            } else {
                JSONObject obj = new JSONObject(s);
                Object credentials = obj.get("Credentials");
                JSONObject jObject = new JSONObject(credentials.toString());
                String userID = (String) jObject.get("ID");
                String password = (String) jObject.get("passwd");
                String title = "";
                //print out the title for chief and associate installer
                if (userID.equals("Barack")) {
                    title = "Chief Sensor Installer";
                } else {
                    title = "Associate Sensor Installer";
                }
                System.out.print(" from " + userID + "," + title);
                byte[] original = "Illegal ID or Password".getBytes();
                if (!InstallerServerAndRSA.salts.containsKey(userID)) {
                    out.write(tea.encrypt(original));
                } else {
                    int val = InstallerServerAndRSA.salts.get(userID);
                    MessageDigest digest = MessageDigest.getInstance("SHA-256");
                    byte[] hash = digest.digest((val + password + "").getBytes(StandardCharsets.UTF_8));
                    if (!printHexBinary(hash).equals(InstallerServerAndRSA.hashValues.get(userID))) {
                        //if the hashed value of the entered password plus the salt stored is not equals to the hashvalue stored
                        //it means the password is not legal
                        out.write(tea.encrypt(original));
                        System.out.println();
                        System.out.println("Illegal Password attempt. This may be an attack.");
                    } else {
                        int id = Integer.parseInt(obj.get("Sensor ID").toString());
                        String latitude = obj.get("Latitude").toString();
                        String longitude = (String) obj.get("Longitude").toString();
                        String location = longitude + "," + latitude + "," + "0.00000";

                        if (InstallerServerAndRSA.sensors.containsKey(id)) {
                            //only barack has the right to move a sensor
                            if(userID.equals("Barack")){
                                //store the userID plus location as the value in the map, sensor id as the key
                                InstallerServerAndRSA.sensors.put(id, userID + "," + location);
                            System.out.println(", a sensor has been moved");
                            out.write(tea.encrypt("Thank you. The sensor’s new location was securely transmitted to GunshotSensing Inc.".getBytes()));
                            }else{
                            out.write(tea.encrypt("Not authorized to move.".getBytes()));
                            }
                            //not existent ever, then add it to hash map
                        } else {
                            InstallerServerAndRSA.sensors.put(id, userID + "," + location);
                            System.out.println();
                            byte[] output = tea.encrypt("Thank you. The sensor’s location was securely transmitted to GunshotSensing Inc.".getBytes());
                            out.write(output);
                            out.flush();
                        }
                    }
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
     * method for creating a kml file
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
        for (Map.Entry<Integer, String> entry : InstallerServerAndRSA.sensors.entrySet()) {
            String microphone = "Microphone " + entry.getKey();//the key is the sensor id
            kml += "<Placemark>\n"
                    + "<name>" + microphone + "</name>\n"
                    //first value of the entry value is the installer id
                    + "<description>" + entry.getValue().split(",")[0] + "</description>\n"
                    + "<styleUrl>#style1</styleUrl>\n"
                    + "<Point>\n"
                    //second and third value of the entry value is the location
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
