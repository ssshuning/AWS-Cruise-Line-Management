package ProjectTask2;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.bson.Document;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Servlet to get to the dashboard page
 * @author apple
 */
@WebServlet(urlPatterns = {"/DashBoardServlet"})
public class DashBoardServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        MongoClientURI connectionString = new MongoClientURI("mongodb://shuningc:951108csn@ds139523.mlab.com:39523/project4");
        MongoClient mongoClient = new MongoClient(connectionString);
        MongoDatabase db = mongoClient.getDatabase("project4");
        MongoCollection<org.bson.Document> collection = db.getCollection("Project4Task2");
        MongoCursor<Document> cursor = collection.find().iterator();
        StringBuilder jsonString = new StringBuilder();
        Double maxPrice = 0.0;
        Double maxTrackPrice = 0.0;
        //this map is used to compare the most frequently searched country
        Map<String, Integer> map = new HashMap<>();
        try {
            while (cursor.hasNext()) {
                //transfer the json string to a json object and use & to connect each field
                //and use ";" to connect each record
                JSONObject tmp = new JSONObject(cursor.next().toJson());
                jsonString.append(tmp.getString("Timestamp")).append("&");
                jsonString.append(tmp.getString("CollectionName")).append("&");
                jsonString.append(tmp.getString("TrackName")).append("&");
                jsonString.append(tmp.getString("CollectionPrice")).append("&");
                Double collectionPrice = Double.parseDouble(tmp.getString("CollectionPrice"));
                maxPrice = collectionPrice > maxPrice ? collectionPrice : maxPrice;
                jsonString.append(tmp.getString("TrackPrice")).append("&");
                Double trackPrice = Double.parseDouble(tmp.getString("TrackPrice"));
                maxTrackPrice = trackPrice > maxTrackPrice ? trackPrice : maxTrackPrice;
                String country = tmp.getString("Country");
                jsonString.append(country).append(";");
                if (map.containsKey(country)) {
                    map.put(country, map.get(country) + 1);
                }else{
                    map.put(country,1);
                }
            }
        } catch (JSONException ex) {
            Logger.getLogger(DashBoardServlet.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            cursor.close();
        }
        //use an arraylist to implement the comparator to get the most frequently appearing country
        List<Map.Entry<String, Integer>> countries = new ArrayList<>(map.entrySet());
        Collections.sort(countries, (Map.Entry<String, Integer> countries1, Map.Entry<String, Integer> countries2) -> countries2.getValue().compareTo(countries1.getValue()));
        for (Map.Entry<String, Integer> entry : countries) {
            System.out.println(entry.getKey() + ":" + entry.getValue());
        }
        request.setAttribute("mostCountry",countries.get(0));
        request.setAttribute("jsonString", jsonString.toString());
        request.setAttribute("maxCollectionPrice", maxPrice);
        request.setAttribute("maxTrackPrice", maxTrackPrice);
        RequestDispatcher view = request.getRequestDispatcher("dashboard.jsp");
        view.forward(request, response);
    }

}
