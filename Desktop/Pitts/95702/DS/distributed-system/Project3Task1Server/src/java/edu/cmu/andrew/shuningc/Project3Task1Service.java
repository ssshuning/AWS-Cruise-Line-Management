/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.cmu.andrew.shuningc;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import javax.jws.WebService;
import javax.jws.WebMethod;

/**
 *
 * @author apple
 */
@WebService(serviceName = "Project3Task1Service")
public class Project3Task1Service {

    private static BlockChain bc;

    public Project3Task1Service() throws NoSuchAlgorithmException {
        bc = new BlockChain();
        Block genesis = new Block(0, new Timestamp(System.currentTimeMillis()), "Genesis", 2, "");
        bc.addBlock(genesis);
    }

    /**
     * This is a sample web service operation
     *
     * @param transaction
     * @param level
     * @throws java.security.NoSuchAlgorithmException
     */
    @WebMethod(operationName = "addBlock")
    public String addBlock(String transaction, int level) throws NoSuchAlgorithmException {
        String message = "";
        long t1 = System.currentTimeMillis();
        String[] data = transaction.split("#");
        BigInteger e = new BigInteger("65537");
        BigInteger n = new BigInteger("2688520255179015026237478731436571621031218154515572968727588377065598663770912513333018006654248650656250913110874836607777966867106290192618336660849980956399732967369976281500270286450313199586861977623503348237855579434471251977653662553");
        BigInteger encrypted = new BigInteger(data[1]);
        BigInteger decrytped = encrypted.modPow(e, n);
        String info = data[0];
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(info.getBytes(StandardCharsets.UTF_8));
        byte[] dest = new byte[hash.length + 1];
        System.arraycopy(hash, 0, dest, 1, hash.length);
        dest[0] = 0;
        if (decrytped.equals(new BigInteger(dest))) {
            Block block = new Block(bc.getChainSize(), new Timestamp(System.currentTimeMillis()), transaction, level, bc.getLatestBlock().proofOfWork());
            bc.addBlock(block);
            System.out.println("Jiayihui");
            long t2 = System.currentTimeMillis();
            message = "Total execution time required to add this block was " + (t2 - t1) + " milliseconds";
        } else {
            message = "Your transaction is not accepted";
        }
        return message;

    }
    /**
     * web method verify
     * @return return the boolean value
     * @throws NoSuchAlgorithmException exception thrown 
     */
    @WebMethod(operationName = "verify")
    public boolean verify() throws NoSuchAlgorithmException {
        return bc.isChainValid();
    }
    /**
     * web method view
     * @return return the contents of the block chain
     * @throws NoSuchAlgorithmException exception thrown
     */
    @WebMethod(operationName = "view")
    public String view() throws NoSuchAlgorithmException {
        System.out.println("View the blockchain");
        return bc.toString();
    }
}
