package com.cwnu.io;


import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;

/**
 * Created by rey on 2018/2/5.
 */
public class SampleMaker {
    public static void main(String[] args) throws IOException {
        byte[] buffer = new byte[1024 * 1024 * 10];
        try (FileOutputStream fos = new FileOutputStream("/Users/yangli/Downloads/sample.txt")) {
            Random random = new Random();
            for (int i = 0; i < 16; i++) {
                random.nextBytes(buffer);
                fos.write(buffer);
            }
        }
    }
}
