package ru.spbau.mit.java.paradov.test;

import ru.spbau.mit.java.paradov.test.MyJunkClass;
import ru.spbau.mit.java.paradov.test.MyJunkyard;

/** Class for tests. */
public class MyYard {
    private Integer things = 0;

    public MyYard(MyJunkyard junkyard, MyHouse house) {
        things += junkyard.getJunks() + 1;
    }

    public Integer getThings() {
        return things;
    }
}
