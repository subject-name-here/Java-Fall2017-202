package ru.spbau.mit.java.paradov;

import java.io.File;
import java.util.concurrent.RecursiveTask;

public class RecursiveHashCounting extends RecursiveTask<String> {
    /** Files over which we iterate. */
    private File[] files;
    /** Indices in files: [ind1, ind2). */
    private int ind1;
    private int ind2;

    public RecursiveHashCounting(File[] files, int ind1, int ind2) {
        this.files = files;
        this.ind1 = ind1;
        this.ind2 = ind2;
    }

    @Override
    protected String compute() {
        StringBuilder sb = new StringBuilder();

        if (ind2 - ind1 == 1) {
            if (files[ind1].isDirectory()) {
                File[] fs = files[ind1].listFiles();
                sb.append(files[ind1].getName());

                if (fs.length == 1) {
                    RecursiveHashCounting t = new RecursiveHashCounting(fs, 0, 1);
                    t.fork();
                    sb.append(t.join());
                } else {
                    RecursiveHashCounting t1 = new RecursiveHashCounting(fs, 0, fs.length / 2);
                    RecursiveHashCounting t2 = new RecursiveHashCounting(fs, fs.length / 2, fs.length);
                    t1.fork();
                    t2.fork();

                    sb.append(t1.join());
                    sb.append(t2.join());
                }

                return Util.getHashOfString(sb.toString());

            } else if (files[ind1].isFile()) {
                return Util.getHashOfFile(files[ind1]);
            }
        }

        int mid = (ind1 + ind2) / 2;
        RecursiveHashCounting t1 = new RecursiveHashCounting(files, ind1, mid);
        RecursiveHashCounting t2 = new RecursiveHashCounting(files, mid, ind2);
        t1.fork();
        t2.fork();

        sb.append(t1.join());
        sb.append(t2.join());

        return sb.toString();
    }
}
