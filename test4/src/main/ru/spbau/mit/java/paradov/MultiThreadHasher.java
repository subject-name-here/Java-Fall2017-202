package ru.spbau.mit.java.paradov;

import java.io.File;
import java.util.concurrent.ForkJoinPool;

public class MultiThreadHasher {
    public static String countHash(File file) {
        String result = "";
        File[] fs = new File[1];
        fs[0] = file;

        if (file.isDirectory()) {
            ForkJoinPool pool = new ForkJoinPool(4);
            result = pool.invoke(new RecursiveHashCounting(fs, 0, 1));
        } else if (file.isFile()) {
            // If it's file, we need no multithreads.
            result = Util.getHashOfFile(file);
        }

        return result;
    }
}
