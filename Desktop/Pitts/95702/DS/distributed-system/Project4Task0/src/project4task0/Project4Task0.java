/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project4task0;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author apple
 */
public class Project4Task0 {

    public static void main(String[] args) throws IOException, JSONException {
        URL apple = new URL("https://itunes.apple.com/search?term=Taylor+Swift&limit=1");
        BufferedReader in = new BufferedReader(
                new InputStreamReader(apple.openStream()));
        String inputLine;
        StringBuilder json = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            json.append(inputLine);
        }
        in.close();
        System.out.println(json.toString());
        JSONObject object = new JSONObject(json.toString());
        JSONArray arr = object.getJSONArray("results");
        String videoUrl = "";
        for (int i = 0; i < arr.length(); i++) {
            videoUrl = (String)arr.getJSONObject(i).getString("previewUrl");
        }
        System.out.println(videoUrl);
    }

}
