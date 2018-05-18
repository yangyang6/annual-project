package com.cwnu.blockchain;

/**
 * Created by rey on 2018/2/16.
 */
public class Block {
    public String hash;
    public String previousHash;
    public String data;
    private long timeStamp;
    private int nonce;

    public Block(String hash, String previousHash) {
        this.hash = hash;
        this.previousHash = previousHash;
        this.timeStamp = System.currentTimeMillis();
        this.hash = calculateHash();
    }


    public String calculateHash(){
        String calculatedHash = StringUtil.appSha256(
                previousHash +
                        Long.toString(timeStamp) +
                        data
        );

        return calculatedHash;
    }

    public void mineBlock(int difficulty) {
        String target = new String(new char[difficulty]).replace('\0', '0'); //Create a string with difficulty * "0"
        while(!hash.substring( 0, difficulty).equals(target)) {
            nonce ++;
            hash = calculateHash();
        }
        System.out.println("Block Mined!!! : " + hash);
    }
}
