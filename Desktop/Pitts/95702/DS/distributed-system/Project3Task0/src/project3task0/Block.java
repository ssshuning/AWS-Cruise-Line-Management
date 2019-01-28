/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project3task0;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import static javax.xml.bind.DatatypeConverter.printHexBinary;

/**
 * block class
 * @author apple
 */
public class Block {

    private int index;
    private Timestamp timestamp;
    private String data;
    private int difficulty;
    private String previousHash;//the SHA256 hash of a block's parent. This is also called a hash pointer
    private BigInteger nonce;
    /**
     * getter for nonce
     * @return big integer returned
     */
    public BigInteger getNonce() {
        return nonce;
    }
    /**
     * setter for nonce
     * @param nonce 
     */
    public void setNonce(BigInteger nonce) {
        this.nonce = nonce;
    }
    /**
     * constructor
     * @param index index of the block
     * @param timestamp time displayed
     * @param data data contained
     * @param difficulty proof work difficulty
     * @param previousHash previous hash value
     */
    public Block(int index, Timestamp timestamp, String data, int difficulty, String previousHash) {
        this.index = index;
        this.timestamp = timestamp;
        this.data = data;
        this.difficulty = difficulty;
        this.previousHash = previousHash;
    }
    /**
     * calculate hash value once
     * @return return the hash value
     * @throws NoSuchAlgorithmException  exception throw
     */
    public String calculateHash() throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash;
        hash = digest.digest((index + "" + timestamp + data + previousHash + nonce.toString() + difficulty).getBytes(StandardCharsets.UTF_8));
        return printHexBinary(hash);
    }

    public String proofOfWork() throws NoSuchAlgorithmException {
        nonce = BigInteger.ZERO;
        String s = calculateHash();
        String prefix = "";
        for (int i = 0; i < getDifficulty(); i++) {
            prefix += "0";
        }
        while (!s.substring(0, difficulty).equals(prefix)) {
            nonce = nonce.add(BigInteger.ONE);
            s = calculateHash();
        }
        return s;
    }
    /**
     * getter for index
     * @return return the integer value
     */ 
    public int getIndex() {
        return index;
    }
    /**
     * getter for time stamp
     * @return return the time stamp object
     */
    public Timestamp getTimestamp() {
        return timestamp;
    }
    /**
     * getter for the data
     * @return return the string data
     */
    public String getData() {
        return data;
    }
    /**
     * getter for difficulty
     * @return return the integer value
     */
    public int getDifficulty() {
        return difficulty;
    }
    /**
     * getter for the previous hash
     * @return return the string value
     */
    public String getPreviousHash() {
        return previousHash;
    }
    /**
     * setter for the index
     * @param index integer value passed
     */
    public void setIndex(int index) {
        this.index = index;
    }
    /**
     * setter for the timestamp
     * @param timestamp object passed
     */
    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }
    /**
     * setter for data
     * @param data string value passed
     */
    public void setData(String data) {
        this.data = data;
    }
    /**
     * setter for difficulty
     * @param difficulty integer value passed
     */
    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }
    /**
     * setter for the hash
     * @param previousHash string passed
     */
    public void setPreviousHash(String previousHash) {
        this.previousHash = previousHash;
    }
    /**
     * override toString() method
     * @return return the string
     */
    @Override
    public String toString() {
        String s = "";
        s += "{\"index\" :" + getIndex() + ",\"time stamp \" : " + getTimestamp() + ",\"Tx \": " + getData() + ",\"PrevHash\" : "
                + getPreviousHash() + ",\"nonce\" : " + getNonce() + ",\"difficulty\" : " + getDifficulty() + "},";
        return s; 
    }

}
