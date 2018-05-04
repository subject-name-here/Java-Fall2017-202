package ru.spbau.mit.java.paradov;

import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;

import static org.junit.Assert.*;

public class ClientTest {
    private static final int PORT_NUMBER = 22229;

    @Test
    public void testGetList() {
        Server server = new Server(22229);
        Thread serverThread = new Thread(server::run);
        serverThread.start();

        String query = "1 src" + File.separator + "test" + File.separator + "resources" + File.separator + "dir1\n e";
        ByteArrayInputStream bais = new ByteArrayInputStream(query.getBytes());
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        Client client = new Client("localhost", PORT_NUMBER);
        client.run(bais, baos);

        String expected = "3\n" + "folder true\n" + "kek1 false\n" + "kek2 false\n\n";

        assertEquals(expected, baos.toString());

        serverThread.interrupt();
    }
}