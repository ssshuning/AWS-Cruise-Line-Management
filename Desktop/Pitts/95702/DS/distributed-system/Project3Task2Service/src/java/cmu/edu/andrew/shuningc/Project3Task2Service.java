/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cmu.edu.andrew.shuningc;

import java.io.IOException;
import java.io.StringReader;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import javax.jws.WebMethod;
import javax.jws.WebService;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.CharacterData;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * service method
 * @author apple
 */
@WebService(serviceName = "Project3Task2Service")
public class Project3Task2Service {

    private static BlockChain bc;
    /**
     * public constructor initializing by adding the first block
     * @throws NoSuchAlgorithmException exception thrown
     */
    public Project3Task2Service() throws NoSuchAlgorithmException {
        bc = new BlockChain();
        Block genesis = new Block(0, new Timestamp(System.currentTimeMillis()), "Genesis", 2, "");
        bc.addBlock(genesis);
    }

    /**
     * this is the deal message web method on the server side
     */
    @WebMethod(operationName = "dealMessage")
    public String dealMessage(String message) throws ParserConfigurationException, SAXException, IOException, NoSuchAlgorithmException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        InputSource is = new InputSource();
        is.setCharacterStream(new StringReader(message));
        Document sensorMessage;
        sensorMessage = builder.parse(is);
        sensorMessage.getDocumentElement().normalize();
        NodeList nodes = sensorMessage.getElementsByTagName("operation");
        String result = "";
        //get the nodes
        //but in our situation, only one node existent
        for (int i = 0; i < nodes.getLength(); i++) {
            Element element = (Element) nodes.item(0);
            NodeList name = element.getElementsByTagName("name");
            Element line = (Element) name.item(0);
            //get the operation name
            String operation = ((CharacterData) line.getFirstChild()).getData();
            switch (operation) {
                case "verify":
                    if (bc.isChainValid()) {
                        result = "The verify result is true";
                    } else {
                        result = "The verify result is false";
                    }
                    return result;

                case "view":
                    return bc.toString();
                    
                case "addBlock":
                    result = "";
                    //get the transaction data and difficulty level by get elements by tag name method
                    NodeList transaction = element.getElementsByTagName("transaction");
                    line = (Element) transaction.item(0);
                    String trans = ((CharacterData) line.getFirstChild()).getData();
                    NodeList difficulty = element.getElementsByTagName("difficulty");
                    line = (Element) difficulty.item(0);
                    String diff = ((CharacterData) line.getFirstChild()).getData();
                    int level = Integer.parseInt(diff);
                    long t1 = System.currentTimeMillis();
                    String[] data = trans.split("#");
                    BigInteger e = new BigInteger("65537");
                    BigInteger n = new BigInteger("2688520255179015026237478731436571621031218154515572968727588377065598663770912513333018006654248650656250913110874836607777966867106290192618336660849980956399732967369976281500270286450313199586861977623503348237855579434471251977653662553");
                    BigInteger encrypted = new BigInteger(data[1]);
                    BigInteger decrytped = encrypted.modPow(e, n);
                    String info = data[0];
                    //copy the byte array to the one larger than original size
                    MessageDigest digest = MessageDigest.getInstance("SHA-256");
                    byte[] hash = digest.digest(info.getBytes(StandardCharsets.UTF_8));
                    byte[] copyHash = new byte[hash.length + 1];
                    System.arraycopy(hash, 0, copyHash, 1, hash.length);
                    copyHash[0] = 0;
                    if (decrytped.equals(new BigInteger(copyHash))) {
                        Block block = new Block(bc.getChainSize(), new Timestamp(System.currentTimeMillis()), trans, level, bc.getLatestBlock().proofOfWork());
                        bc.addBlock(block);
                        long t2 = System.currentTimeMillis();
                        result = "Total execution time required to add this block was " + (t2 - t1) + " milliseconds";
                    } else {
                        result = "Your transaction is not accepted";
                    }
                    System.out.println(result);
                    return result;

            }
        }
        return result;
    }
}
