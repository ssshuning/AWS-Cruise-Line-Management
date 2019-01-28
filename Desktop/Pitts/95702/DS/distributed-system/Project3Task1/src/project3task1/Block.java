/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project3task1;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import static javax.xml.bind.DatatypeConverter.printHexBinary;

/**
 *
 * @author apple
 */
public class Block {

    private int index;
    private Timestamp timestamp;
    private String data;
    private int difficulty;
    private String previousHash;//the SHA256 hash of a block's parent. This is also called a hash pointer
    private BigInteger nonce;

    public BigInteger getNonce() {
        return nonce;
    }

    public void setNonce(BigInteger nonce) {
        this.nonce = nonce;
    }

    public Block(int index, Timestamp timestamp, String data, int difficulty, String previousHash) {
        this.index = index;
        this.timestamp = timestamp;
        this.data = data;
        this.difficulty = difficulty;
        this.previousHash = previousHash;
    }

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

    public int getIndex() {
        return index;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public String getData() throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash;
        hash = digest.digest(data.getBytes(StandardCharsets.UTF_8));
        byte[] dest = new byte[hash.length + 1];
        System.arraycopy(hash, 0, dest, 1, hash.length);
        dest[0] = 0;
        BigInteger bi = new BigInteger(dest);
        BigInteger d = new BigInteger("339177647280468990599683753475404338964037287357290649 639740920420195763493261892674937712727426153831055473238029100 340967145378283022484846784794546119352371446685199413453480215 164979267671668216248690393620864946715883011485526549108913");
        BigInteger n = new BigInteger("268852025517901502623747873143657162103121815451557296 872758837706559866377091251333301800665424865065625091311087483 660777796686710629019261833666084998095639973296736997628150027 0286450313199586861977623503348237855579434471251977653662553");
        BigInteger encrypted = bi.modPow(d, n);
        return data+"#"+encrypted.toString();
    }

    public int getDifficulty() {
        return difficulty;
    }

    public String getPreviousHash() {
        return previousHash;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public void setData(String data) {
        this.data = data;
    }

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }

    public void setPreviousHash(String previousHash) {
        this.previousHash = previousHash;
    }

    @Override
    public String toString() {
        String s = "";
        s += "index:" + index + ",timestamp:" + timestamp.getTime() + ",data:" + data
                + ",difficulty:" + difficulty + ",previsousHash:" + previousHash + ",nonce:" + nonce;
        return s; //To change body of generated methods, choose Tools | Templates.
    }

}
