package ru.spbau.mit.java.paradov.test;

public class MyNowhere implements MyCity {
    Integer people = 0;
    public MyNowhere() {
        people = -1;
    }

    public Integer getPeople() {
        return people;
    }
}
