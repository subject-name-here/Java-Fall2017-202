package ru.spbau.mit.java.paradov.test;

import ru.spbau.mit.java.paradov.test.MyJunkClass;

/** Class for testing. */
public class MyJunkyard {
    private Integer junks = 2;

    public MyJunkyard(MyJunkClass junk) {
        junks += junk.getA();
    }

    public Integer getJunks() {
        return junks;
    }
}
