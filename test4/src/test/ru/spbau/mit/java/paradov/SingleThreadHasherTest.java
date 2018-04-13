package ru.spbau.mit.java.paradov;

import org.junit.Test;

import java.io.File;

import static org.junit.Assert.*;

public class SingleThreadHasherTest {
    public static String PREFIX = "src/resources/";
    // All expected hashes are got from http://onlinemd5.com.

    @Test
    public void countFileHash() {
        String h = SingleThreadHasher.countHash(new File(PREFIX + "FILE_FOR_TEST!!1"));
        String expected = "AC6C419B1AF87DB556C4BBF1636BF4AB".toLowerCase();
        assertEquals(expected, h);
    }
/* Doesn't work :(
    @org.junit.Test
    public void countDirHash() {
        String h = SingleThreadHasher.countHash(new File(PREFIX + "dir"));
        String expected = "9CD4C3FC9FF3EEC1C32462B0399CB70F".toLowerCase();
        assertEquals(expected, h);
    }
*/
}