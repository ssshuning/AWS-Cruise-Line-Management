/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cmu.edu.andrew.shuningc;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import static javax.xml.bind.DatatypeConverter.printHexBinary;

/**
 * block chain class
 * @author apple
 */
public class BlockChain {
    
    private List<Block> blocks;
    private String chainHash;
    /**
     * getter for blocks
     * @return return the list
     */
    public List<Block> getBlocks() {
        return blocks;
    }
    /**
     * setter for blocks
     * @param blocks list passed
     */
    public void setBlocks(List<Block> blocks) {
        this.blocks = blocks;
    }
    /**
     * getter for chain hash
     * @return string returned
     */
    public String getChainHash() {
        return chainHash;
    }
    /**
     * setter for chain hash
     * @param chainHash string value passed
     */
    public void setChainHash(String chainHash) {
        this.chainHash = chainHash;
    }
    
    /**
     * constructor
     */
    public BlockChain() {
        blocks = new ArrayList<>();
        chainHash = "";
    }
    /**
     * add block method
     * @param newBlock a block passed
     * @throws NoSuchAlgorithmException exception thrown
     */
    public void addBlock(Block newBlock) throws NoSuchAlgorithmException {
        long t1 = System.currentTimeMillis();
        blocks.add(newBlock);
        newBlock.setPreviousHash(chainHash);
        chainHash = newBlock.proofOfWork();
        long t2 = System.currentTimeMillis();
        System.out.println("Total execution time required to add this block was " + (t2 - t1) + " milliseconds");
    }
    /**
     * get the size of the block chain
     * @return return the size
     */
    public int getChainSize() {
        return blocks.size();
    }
    /**
     * get the latest block
     * @return return the block
     */
    public Block getLatestBlock() {
        return blocks.get(blocks.size() - 1);
    }
    /**
     * getter for the time
     * @return return the timestamp object
     */
    public Timestamp getTime() {
        Timestamp timeStamp = new Timestamp(System.currentTimeMillis());
        return timeStamp;
    }
    /**
     * number of hashes per second
     * @return return the count
     * @throws NoSuchAlgorithmException exception thrown
     */
    public int hashesPerSecond() throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash;
        Timestamp ts1 = getTime();
        Timestamp ts2 = ts1;
        int i = 0;
        //if the time is less than 1000 milliseconds
        while (ts2.getTime() - ts1.getTime() < 1000) {
            hash = digest.digest("00000000".getBytes(StandardCharsets.UTF_8));
            String s = printHexBinary(hash);
            ts2 = getTime();
            i++;
        }
        return i;
    }
    /**
     * to judge whether the block chain is valid
     * @return return the boolean value
     * @throws NoSuchAlgorithmException exception thrown
     */
    public boolean isChainValid() throws NoSuchAlgorithmException {
        if (getChainSize() == 1) {
            long t1 = System.currentTimeMillis();
            Block tmp = blocks.get(0);
            String s = tmp.proofOfWork();
            String prefix = "";
            for (int i = 0; i < tmp.getDifficulty(); i++) {
                prefix += "0";
            }
            //if the first zeros match the difficulty and the first block's hash value matches the new calculated one
            if (chainHash.substring(0, tmp.getDifficulty()).equals(prefix) && tmp.calculateHash().equals(chainHash)) {
                long t2 = System.currentTimeMillis();
                System.out.println("Chain verification: true");
                System.out.println("Total execution time required to verify the chain was " + (t2 - t1) + " milliseconds");
                return true;
            } else {
                System.out.println(s);
                System.out.println(chainHash);
                long t2 = System.currentTimeMillis();
                if (!chainHash.substring(0, tmp.getDifficulty()).equals(prefix)) {
                    System.out.println("..Improper hash on node 0 Does not begin with " + prefix);
                }
                if (!tmp.calculateHash().equals(chainHash)) {
                    System.out.println("..Improper hash on node 0 Does not have the right hash value");
                }
                System.out.println("Chain verification: false");
                System.out.println("Total execution time required to verify the chain was " + (t2 - t1) + " milliseconds");
                return false;
            }
        }
        //if the block chain size is larger than 1
        if (getChainSize() > 1) {
            long t1 = System.currentTimeMillis();
            for (int i = 0; i < getChainSize() - 1; i++) {
                Block pre = blocks.get(i);
                Block current = blocks.get(i + 1);
                String hash = current.getPreviousHash();
                String prefix = "";
                //get the number of zeros
                for (int j = 0; j < pre.getDifficulty(); j++) {
                    prefix += "0";
                }
                //compare the prefix and the calculated hash value
                if (!hash.substring(0, pre.getDifficulty()).equals(prefix) || !pre.calculateHash().equals(hash)) {
                    long t2 = System.currentTimeMillis();
                    if (!chainHash.substring(0, pre.getDifficulty()).equals(prefix)) {
                        System.out.println("..Improper hash on node " + i + " Does not begin with " + prefix);
                    }
                    if (!pre.calculateHash().equals(chainHash)) {
                        System.out.println("..Improper hash on node " + i + " Does not have the right hash value");
                    }
                    System.out.println("Chain verification: false");
                    System.out.println("Total execution time required to verify the chain was " + (t2 - t1) + " milliseconds");
                    return false;
                    //the last block in the block chain
                } else if (i == getChainSize() - 2) {
                    Block last = blocks.get(getChainSize() - 1);
                    prefix = "";
                    for (int j = 0; j < last.getDifficulty(); j++) {
                        prefix += "0";
                    }
                    //compare the difficulty and the calculated hash
                    if (chainHash.substring(0, last.getDifficulty()).equals(prefix) && last.calculateHash().equals(chainHash)) {
                        long t2 = System.currentTimeMillis();
                        if (!chainHash.substring(0, pre.getDifficulty()).equals(prefix)) {
                            System.out.println("..Improper hash on node " + (getChainSize()-1) + " Does not begin with " + prefix);
                        }
                        if (!pre.calculateHash().equals(chainHash)) {
                            System.out.println("..Improper hash on node " + (getChainSize()-1) + " Does not have the right hash value");
                        }
                        System.out.println("Chain verification: true");
                        System.out.println("Total execution time required to verify the chain was " + (t2 - t1) + " milliseconds");
                        return true;
                    } else {
                        long t2 = System.currentTimeMillis();
                        System.out.println("Chain verification: false");
                        System.out.println("Total execution time required to verify the chain was " + (t2 - t1) + " milliseconds");
                        return false;
                    }
                }
            }
        }
        return false;
    }
    /**
     * repair the corrupted chain
     * @throws NoSuchAlgorithmException exception thrown
     */
    public void repairChain() throws NoSuchAlgorithmException {
        long t1 = System.currentTimeMillis();
        for (int i = 1; i < getChainSize(); i++) {
            Block block1 = blocks.get(i - 1);
            Block block2 = blocks.get(i);
            if (!block1.calculateHash().equals(block2.getPreviousHash())) {
                block2.setPreviousHash(block1.proofOfWork());
            }
        }
        Block block = blocks.get(getChainSize() - 1);
        if (!block.calculateHash().equals(chainHash)) {
            chainHash = block.proofOfWork();
        }
        long t2 = System.currentTimeMillis();
        System.out.println("Total execution time required to repair the chain was " + (t2 - t1) + " milliseconds");
    }
    /**
     * main method to run the operation
     * @param args command line parameter
     * @throws NoSuchAlgorithmException exception thrown
     */
    public static void main(String[] args) throws NoSuchAlgorithmException {
        BlockChain bc = new BlockChain();
        Block genesis = new Block(0, new Timestamp(System.currentTimeMillis()), "Genesis", 2, "");
        //add the first block
        bc.addBlock(genesis);
        Scanner scanner = new Scanner(System.in);
        int selection = -1;
        while (selection != 6) {
            System.out.println("0.View basic blockchain status.");
            System.out.println("1.Add a transaction to the blockchain.");
            System.out.println("2.Verify the blockchain.");
            System.out.println("3.View the blockchain.");
            System.out.println("4.Corrupt the chain.");
            System.out.println("5.Hide the corruption by repairing the chain.");
            System.out.println("6.Exit.");
            selection = scanner.nextInt();
            scanner.nextLine();
            switch (selection) {
                case 0:
                    System.out.println("Current size of chain: " + bc.getChainSize());
                    System.out.println("Current hashes per second by this machine: " + bc.hashesPerSecond());
                    break;
                case 1:
                    System.out.println("Enter difficulty > 0");
                    int level = scanner.nextInt();
                    scanner.nextLine();
                    System.out.println("Enter transaction");
                    String transaction = scanner.nextLine();
                    String s = bc.getLatestBlock().proofOfWork();
                    Block block = new Block(bc.getChainSize(), new Timestamp(System.currentTimeMillis()), transaction, level, s);
                    bc.addBlock(block);
                    break;
                case 2:
                    System.out.println("Verifying entire chain");
                    bc.isChainValid();
                    break;
                case 3:
                    System.out.println("View the blockchain");
                    System.out.println("{\"ds_chain\" : [ ");
                    for (int i = 0; i < bc.blocks.size(); i++) {
                        Block tmp = bc.blocks.get(i);
                        System.out.println("{\"index\" :" + tmp.getIndex() + ",\"time stamp \" : " + tmp.getTimestamp() + ",\"Tx \": " + tmp.getData() + ",\"PrevHash\" : "
                                + tmp.getPreviousHash() + ",\"nonce\" : " + tmp.getNonce() + ",\"difficulty\" : " + tmp.getDifficulty() + "},");
                    }
                    System.out.println("],");
                    System.out.println("\"chainHash\": " + bc.chainHash + "}");
                    break;
                case 4:
                    System.out.println("Enter index for corruption");
                    int index = scanner.nextInt();
                    scanner.nextLine();
                    System.out.println("Enter the data to be placed in the block");
                    String content = scanner.nextLine();
                    bc.blocks.get(index).setData(content);
                    System.out.println("Block " + index + " now holds " + content);
                    break;
                case 5:
                    System.out.println("Repairing entire chain");
                    bc.repairChain();
                    break;
                case 6:
                    break;
            }

        }

    }
    /**
     * override toString() method
     * @return 
     */
    @Override
    public String toString() {
        String s = "{\"ds_chain\" : [ ";
        for (int i = 0; i < getBlocks().size(); i++) {
            Block tmp = getBlocks().get(i);
            s += tmp.toString();
        }
        s += "],";
        s += "\"chainHash\": " + getChainHash() + "}";
        return s;
    }

}
