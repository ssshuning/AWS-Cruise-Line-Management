/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project4task0db;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import java.util.Scanner;
import org.bson.Document;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author apple
 */
public class Project4Task0DB {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws JSONException {

        MongoClientURI connectionString = new MongoClientURI("mongodb://shuningc:951108csn@ds139523.mlab.com:39523/project4");
        MongoClient mongoClient = new MongoClient(connectionString);
        MongoDatabase db = mongoClient.getDatabase("project4");
        MongoCollection<org.bson.Document> collection = db.getCollection("MobileRequest");
        
        Scanner scanner = new Scanner(System.in);
        System.out.println("What's your name?");
        String name = scanner.nextLine();
        
        System.out.println("How old are you?");
        int age = scanner.nextInt();
        scanner.nextLine();
        Document document1 = new Document("name", name).append("age", age);
        
        collection.insertOne(document1);
        System.out.println("What's your friend's name?");
        name = scanner.nextLine();
        
        System.out.println("How old are your friend?");
        age = scanner.nextInt();
        scanner.nextLine();
        Document document2 = new Document("name", name).append("age", age);
        document2.put("name", name);
        document2.put("age", age);
        collection.insertOne(document2);
        MongoCursor<Document> cursor = collection.find().iterator();
        try {
            while (cursor.hasNext()) {
//                JSONObject tmp = new JSONObject(cursor.next().toJson());
//                System.out.println("name:"+tmp.getString("name")+"age:"+tmp.getString("age"));
               System.out.println(cursor.next().toJson());
            }
        } finally {
            cursor.close();
        }
    }

}
