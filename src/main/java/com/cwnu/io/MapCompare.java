package com.cwnu.io;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * Created by rey on 2018/2/5.
 * 使用nio来读取 ，只用了362ms
 */
public class MapCompare {
    public static void main(String[] args) throws IOException {
        long start = System.nanoTime();
        FileChannel ch1 = new RandomAccessFile("/Users/yangli/Downloads/sample.txt", "r").getChannel();
        FileChannel ch2 = new RandomAccessFile("/Users/yangli/Downloads/sample-copy.txt", "r").getChannel();
        if (ch1.size() != ch2.size()) {
            System.out.println("Files have different length");
            return;
        }
        long size = ch1.size();
        ByteBuffer m1 = ch1.map(FileChannel.MapMode.READ_ONLY, 0L, size);
        ByteBuffer m2 = ch2.map(FileChannel.MapMode.READ_ONLY, 0L, size);
        for (int pos = 0; pos < size; pos++) {
            if (m1.get(pos) != m2.get(pos)) {
                System.out.println("Files differ at position " + pos);
                return;
            }
        }
        System.out.println("Files are identical, you can delete one of them.");
        long end = System.nanoTime();
        System.out.print("Execution time: " + (end - start) / 1000000 + "ms");
    }
}
