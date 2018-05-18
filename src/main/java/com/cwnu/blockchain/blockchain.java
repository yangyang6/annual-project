package com.cwnu.blockchain;

import com.alibaba.fastjson.JSON;

import java.util.ArrayList;

/**
 * Created by rey on 2018/2/16.
 */
public class blockchain {

    public static ArrayList<Block> blockchain = new ArrayList<Block>();

    public static void main(String[] args) {
        //add our blocks to the blockchain ArrayList:
        blockchain.add(new Block("Hi im the first block", "0"));
        blockchain.add(new Block("Yo im the second block",blockchain.get(blockchain.size()-1).hash));
        blockchain.add(new Block("Hey im the third block",blockchain.get(blockchain.size()-1).hash));

        String blockChainJson = JSON.toJSONString(blockchain);
        System.out.println(blockChainJson);
    }


    public static Boolean isChainValid() {
        Block currentBlock;
        Block previousBlock;

        //loop through blockchain to check hashes:
        for(int i=1; i < blockchain.size(); i++) {
            currentBlock = blockchain.get(i);
            previousBlock = blockchain.get(i-1);
            //compare registered hash and calculated hash:
            if(!currentBlock.hash.equals(currentBlock.calculateHash()) ){
                System.out.println("Current Hashes not equal");
                return false;
            }
            //compare previous hash and registered previous hash
            if(!previousBlock.hash.equals(currentBlock.previousHash) ) {
                System.out.println("Previous Hashes not equal");
                return false;
            }
        }
        return true;
    }
}
