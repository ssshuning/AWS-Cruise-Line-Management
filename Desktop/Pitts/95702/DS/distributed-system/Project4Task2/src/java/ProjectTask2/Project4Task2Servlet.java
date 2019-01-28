/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ProjectTask2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import java.sql.Timestamp;
import org.bson.Document;

/**
 * servlet to get parameters from api and return the contents to android application
 * @author apple
 */
@WebServlet(urlPatterns = {"/Project4Task2Servlet/*"})
public class Project4Task2Servlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        System.out.println("Console: doGet visited");
        //connect to the database
        MongoClientURI connectionString = new MongoClientURI("mongodb://shuningc:951108csn@ds139523.mlab.com:39523/project4");
        MongoClient mongoClient = new MongoClient(connectionString);
        MongoDatabase db = mongoClient.getDatabase("project4");
        MongoCollection<org.bson.Document> collection = db.getCollection("Project4Task2");
        StringBuilder json = new StringBuilder();
        String searchTerm = (request.getPathInfo()).substring(1);
        String timeStamp = new Timestamp(System.currentTimeMillis()).toString();
        //replace the space to "+"
        searchTerm = searchTerm.replaceAll(" ", "+");
        URL apple = new URL("https://itunes.apple.com/search?term=" + searchTerm + "&limit=1");
        HttpURLConnection conn = (HttpURLConnection) apple.openConnection();
        conn.setRequestMethod("GET");
        // tell the server what format we want back
        BufferedReader in = new BufferedReader(new InputStreamReader(
                conn.getInputStream()));
        String inputLine;
        while ((inputLine = in.readLine()) != null) {
            json.append(inputLine);
            System.out.println(inputLine);
        }
        in.close();
        String videoUrl = "";
        String artistName = "";
        String collectionName = "";
        String trackName = "";
        String country = "";
        String trackPrice = "";
        String collectionPrice = "";
        String currency = "";
        try {
            JSONObject object = new JSONObject(json.toString());
            JSONArray arr = object.getJSONArray("results");
            for (int i = 0; i < arr.length(); i++) {
                videoUrl = arr.getJSONObject(i).getString("previewUrl");
                artistName = arr.getJSONObject(i).getString("artistName");
                collectionName = arr.getJSONObject(i).getString("collectionName");
                trackName = arr.getJSONObject(i).getString("trackName");
                country = arr.getJSONObject(i).getString("country");
                trackPrice = arr.getJSONObject(i).getString("trackPrice");
                collectionPrice = arr.getJSONObject(i).getString("collectionPrice");
                currency = arr.getJSONObject(i).getString("currency");
            }

        } catch (JSONException e) {
            Logger.getLogger(Project4Task2Servlet.class.getName()).log(Level.SEVERE, null, e);
        }
        //add the parameters to the document
        Document document1 = new Document("Timestamp", timeStamp);
        document1.append("CollectionName", collectionName);
        document1.append("TrackName", trackName);
        document1.append("CollectionPrice", collectionPrice);
        document1.append("TrackPrice", trackPrice);
        document1.append("Country", country);
        collection.insertOne(document1);

        response.setContentType("application/json");
        JSONObject video;
        try {
            System.out.println(videoUrl);
            System.out.println(artistName);
            video = new JSONObject("{\"previewUrl\":\"" + videoUrl + "\","
                    + "\"artistName\":\"" + artistName + "\","
                    + "\"collectionName\":\"" + collectionName + "\","
                    + "\"trackName\":\"" + trackName + "\","
                    + "\"country\":\"" + country + "\","
                    + "\"trackPrice\":\"" + trackPrice + "\","
                    + "\"collectionPrice\":\"" + collectionPrice + "\","
                    + "\"currency\":\"" + currency + "\"}");

// Get the printwriter object from response to write the required json object to the output stream      
            PrintWriter out = response.getWriter();
// Assuming your json object is **jsonObject**, perform the following, it will return your json object 
            out.print(video);
            out.flush();
        } catch (JSONException ex) {
            Logger.getLogger(Project4Task2Servlet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
