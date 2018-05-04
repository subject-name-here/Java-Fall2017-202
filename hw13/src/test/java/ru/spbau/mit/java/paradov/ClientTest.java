package ru.spbau.mit.java.paradov;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

import static org.junit.Assert.*;

public class ClientTest {
    private static final int PORT_NUMBER = 22229;
    private static Thread serverThread;

    @BeforeClass
    public static void setUpServer() {
        Server server = new Server(22229);
        serverThread = new Thread(server::run);
        serverThread.start();
    }

    @AfterClass
    public static void cleanServer() {
        serverThread.interrupt();
        try {
            FileUtils.cleanDirectory(new File("downloads"));
        } catch (IOException e) {
            System.err.println("Failed to delete objects from \"downloads\". Please, do it manually.");
        } catch (IllegalArgumentException e) {
            // Do nothing: directory wasn't created.
        }
    }

    @Test
    public void testGetList() {
        String query = "1 src" + File.separator + "test" + File.separator + "resources" + File.separator + "dir1\n e";
        ByteArrayInputStream bais = new ByteArrayInputStream(query.getBytes());
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        Client client = new Client("localhost", PORT_NUMBER);
        client.run(bais, baos);

        String expected = "3\n" + "folder true\n" + "kek1 false\n" + "kek2 false\n\n";

        assertEquals(expected, baos.toString());
    }

    @Test
    public void testGetEmptyList() {
        String query = "1 src" + File.separator + "test" + File.separator + "resources" + File.separator + "dir0\n e";
        ByteArrayInputStream bais = new ByteArrayInputStream(query.getBytes());
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        Client client = new Client("localhost", PORT_NUMBER);
        client.run(bais, baos);

        String expected = "0\n\n";

        assertEquals(expected, baos.toString());
    }

    @Test
    public void testGetList2() {
        String query = "1 src" + File.separator + "test" + File.separator + "resources" + File.separator + "dir2\n e";
        ByteArrayInputStream bais = new ByteArrayInputStream(query.getBytes());
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        Client client = new Client("localhost", PORT_NUMBER);
        client.run(bais, baos);

        String expected = "7\n" + "f1 false\n" + "f2 false\n" + "finf false\n"
                + "k1 true\n" + "k2 true\n" + "k3 true\n"
                + "the file false\n\n";

        assertEquals(expected, baos.toString());
    }

    @Test
    public void testGetFile() {
        String query = "2 src/test/resources/dir1/kek1\n e".replace("/", File.separator);
        ByteArrayInputStream bais = new ByteArrayInputStream(query.getBytes());
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        Client client = new Client("localhost", PORT_NUMBER);
        client.run(bais, baos);


        //assertEquals();
    }
}