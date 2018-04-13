package ru.spbau.mit.java.paradov;

import java.io.File;

public class Main {
    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("You didn't provide argument!");
            return;
        }

        File file = new File(args[0]);

        long startTime1 = System.currentTimeMillis();
        System.out.println(SingleThreadHasher.countHash(file));
        long finishTime1 = System.currentTimeMillis();
        String time1 = Long.toString(finishTime1 - startTime1);

        long startTime2 = System.currentTimeMillis();
        System.out.println(MultiThreadHasher.countHash(file));
        long finishTime2 = System.currentTimeMillis();
        String time2 = Long.toString(finishTime2 - startTime2);

        System.out.println("Hasher with one thread worked for " + time1 + " ms.");
        System.out.println("Hasher with many threads worked for " + time2 + " ms.");
    }
}
