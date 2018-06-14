package ru.spbau.mit.java.paradov;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

import java.io.*;
import java.net.Socket;

import org.apache.commons.io.FileUtils;
import org.junit.rules.TemporaryFolder;

import static org.junit.Assert.*;

/** Tests work of server alone. */
public class ServerTest {
    private static final int PORT_NUMBER = 22229;
    private static final int BUF_SIZE = 1024;
    /** Thread where server is running. */
    private static Thread serverThread;

    /** Line separator for this filesystem. */
    private static String sep = System.lineSeparator();

    /** Launches server before every test. */
    @BeforeClass
    public static void setUpServer() {
        Server server = new Server(PORT_NUMBER);
        serverThread = new Thread(server::run);
        serverThread.start();
    }

    /** Stops server after test.*/
    @AfterClass
    public static void cleanServer() {
        serverThread.interrupt();
    }

    /** Temporary folder for files to list. */
    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    /** Tests listing of files. */
    @Test
    public void testGetList() throws IOException {
        folder.newFile("tfile1");
        folder.newFile("tfile2");
        TemporaryFolder fold = new TemporaryFolder(folder.newFolder("fold"));
        fold.create();
        fold.newFile("never_to_be_shown");

        try (Socket s = new Socket("localhost", PORT_NUMBER);
             DataInputStream dis = new DataInputStream(s.getInputStream());
             DataOutputStream dos = new DataOutputStream(s.getOutputStream())) {

            dos.writeInt(1);
            dos.write(folder.getRoot().getPath().getBytes());
            dos.flush();
            s.shutdownOutput();

            int n = dis.readInt();
            assertEquals(3, n);

            StringBuilder list = readFromDataInputStream(dis);
            String expected = "fold true" + sep + "tfile1 false" + sep + "tfile2 false" + sep;
            assertEquals(expected, list.toString());
        }
    }

    /** Tests listing of empty directory. */
    @Test
    public void testGetEmptyList() throws IOException {
        try (Socket s = new Socket("localhost", PORT_NUMBER);
             DataInputStream dis = new DataInputStream(s.getInputStream());
             DataOutputStream dos = new DataOutputStream(s.getOutputStream())) {

            dos.writeInt(1);
            dos.write(folder.getRoot().getPath().getBytes());
            dos.flush();
            s.shutdownOutput();

            int n = dis.readInt();
            assertEquals(0, n);
        }
    }

    /** Tests listing of files, but it's bigger. */
    @Test
    public void testGetList2() throws IOException {
        folder.newFile("f1");
        folder.newFile("f2");
        folder.newFile("finf");
        folder.newFile("the file");
        TemporaryFolder k1 = new TemporaryFolder(folder.newFolder("k1"));
        TemporaryFolder k2 = new TemporaryFolder(folder.newFolder("k2"));
        TemporaryFolder k3 = new TemporaryFolder(folder.newFolder("k3"));
        k1.create();
        k2.create();
        k3.create();
        k1.newFile("never_to_be_shown");

        try (Socket s = new Socket("localhost", PORT_NUMBER);
             DataInputStream dis = new DataInputStream(s.getInputStream());
             DataOutputStream dos = new DataOutputStream(s.getOutputStream())) {

            dos.writeInt(1);
            dos.write(folder.getRoot().getPath().getBytes());
            dos.flush();
            s.shutdownOutput();

            int n = dis.readInt();
            assertEquals(7, n);

            StringBuilder list = readFromDataInputStream(dis);
            String expected = "f1 false" + sep + "f2 false" + sep + "finf false" + sep
                    + "k1 true" + sep + "k2 true" + sep + "k3 true" + sep
                    + "the file false" + sep;

            assertEquals(expected, list.toString());
        }
    }

    /** Tests getting file from server. */
    @Test
    public void testGetFile() throws IOException {
        String file = "src/test/resources/OrdinaryFile".replace("/", File.separator);
        File f = new File(file);

        try (Socket s = new Socket("localhost", PORT_NUMBER);
             DataInputStream dis = new DataInputStream(s.getInputStream());
             DataOutputStream dos = new DataOutputStream(s.getOutputStream())) {

            dos.writeInt(2);
            dos.write(f.getPath().getBytes());
            dos.flush();
            s.shutdownOutput();

            long n = dis.readLong();
            assertEquals(f.length(), n);

            StringBuilder content = readFromDataInputStream(dis);

            assertEquals(FileUtils.readFileToString(f), content.toString());
        }
    }

    /** Tests response on non-existing file. */
    @Test
    public void testGetNonExistingFile() throws IOException {
        String file = "src/test/resources/NonExistingFile".replace("/", File.separator);
        File f = new File(file);

        try (Socket s = new Socket("localhost", PORT_NUMBER);
             DataInputStream dis = new DataInputStream(s.getInputStream());
             DataOutputStream dos = new DataOutputStream(s.getOutputStream())) {

            dos.writeInt(2);
            dos.write(f.getPath().getBytes());
            dos.flush();
            s.shutdownOutput();

            long n = dis.readLong();
            assertEquals(-1, n);
        }
    }
    
    private StringBuilder readFromDataInputStream(DataInputStream dis) throws IOException {
        StringBuilder sb = new StringBuilder();
        byte[] buf = new byte[BUF_SIZE];
        int readSymbols;
        while ((readSymbols = dis.read(buf)) != -1) {
            sb.append(new String(buf, 0, readSymbols));
        }

        return sb;
    }
}