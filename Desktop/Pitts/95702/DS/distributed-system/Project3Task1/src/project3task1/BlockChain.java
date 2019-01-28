/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project3task1;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import static javax.xml.bind.DatatypeConverter.printHexBinary;

/**
 *
 * @author apple
 */
public class BlockChain {

    private List<Block> blocks;
    private String chainHash;

    public BlockChain() {
        blocks = new ArrayList<>();
        chainHash = "";
    }

    public void addBlock(Block newBlock) throws NoSuchAlgorithmException {
        long t1 = System.currentTimeMillis();
        blocks.add(newBlock);
        newBlock.setPreviousHash(chainHash);
        chainHash = newBlock.proofOfWork();
        long t2 = System.currentTimeMillis();
        System.out.println("Total execution time required to add this block was " + (t2 - t1) + " milliseconds");
    }

    public int getChainSize() {
        return blocks.size();
    }

    public Block getLatestBlock() {
        return blocks.get(blocks.size() - 1);
    }

    public Timestamp getTime() {
        Timestamp timeStamp = new Timestamp(System.currentTimeMillis());
        return timeStamp;
    }

    public int hashesPerSecond() throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash;
        Timestamp ts1 = getTime();
        Timestamp ts2 = ts1;
        int i = 0;
        while (ts2.getTime() - ts1.getTime() < 1000) {
            hash = digest.digest("00000000".getBytes(StandardCharsets.UTF_8));
            String s = printHexBinary(hash);
            ts2 = getTime();
            i++;
        }
        return i;
    }

    public boolean isChainValid() throws NoSuchAlgorithmException {
        if (getChainSize() == 1) {
            long t1 = System.currentTimeMillis();
            Block tmp = blocks.get(0);
            String s = tmp.proofOfWork();
            String prefix = "";
            for (int i = 0; i < tmp.getDifficulty(); i++) {
                prefix += "0";
            }
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
        if (getChainSize() > 1) {
            long t1 = System.currentTimeMillis();
            for (int i = 0; i < getChainSize() - 1; i++) {
                Block pre = blocks.get(i);
                Block current = blocks.get(i + 1);
                String hash = current.getPreviousHash();
                String prefix = "";
                for (int j = 0; j < pre.getDifficulty(); j++) {
                    prefix += "0";
                }
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
                } else if (i == getChainSize() - 2) {
                    Block last = blocks.get(getChainSize() - 1);
                    prefix = "";
                    for (int j = 0; j < last.getDifficulty(); j++) {
                        prefix += "0";
                    }
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

    public static void main(String[] args) throws NoSuchAlgorithmException {
        BlockChain bc = new BlockChain();
        Block genesis = new Block(0, new Timestamp(System.currentTimeMillis()), "Genesis", 2, "");
        bc.addBlock(genesis);
        Scanner scanner = new Scanner(System.in);
        int selection = -1;
        while (selection != 6) {
            System.out.println("1.Add a transaction to the blockchain.");
            System.out.println("2.Verify the blockchain.");
            System.out.println("3.View the blockchain.");
            System.out.println("4.Exit.");
            selection = scanner.nextInt();
            scanner.nextLine();
            switch (selection) {
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

    @Override
    public String toString() {
        String s = "";
        for (Block block : blocks) {
            s += block.toString() + ",";
        }
        return s;
    }

}
