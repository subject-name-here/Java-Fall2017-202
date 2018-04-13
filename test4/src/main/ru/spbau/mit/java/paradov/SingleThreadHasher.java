package ru.spbau.mit.java.paradov;

import java.io.File;

public class SingleThreadHasher {
    public static String countHash(File file) {
        String result = "";

        if (file.isDirectory()) {
            StringBuilder sb = new StringBuilder();
            sb.append(file.getName());

            for (File innerFile : file.listFiles()) {
                sb.append(SingleThreadHasher.countHash(innerFile));
            }

            result = Util.getHashOfString(sb.toString());
        } else if (file.isFile()) {
            result = Util.getHashOfFile(file);
        }


        return result;
    }
}
