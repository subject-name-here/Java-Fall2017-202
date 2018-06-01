package ru.spbau.mit.java.paradov;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

import java.io.*;

import org.apache.commons.io.FileUtils;
import org.junit.rules.TemporaryFolder;

import static org.junit.Assert.*;

public class ClientServerTest {
    private static final int PORT_NUMBER = 22229;
    private static Thread serverThread;
    private static String sep = System.lineSeparator();

    @BeforeClass
    public static void setUpServer() {
        Server server = new Server(PORT_NUMBER);
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

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    @Test
    public void testGetList() throws IOException {
        folder.newFile("tfile1");
        folder.newFile("tfile2");
        TemporaryFolder fold = new TemporaryFolder(folder.newFolder("fold"));
        fold.create();
        fold.newFile("never_to_be_shown");

        String query = "1 " + folder.getRoot().getPath() + sep + "e";
        ByteArrayInputStream bais = new ByteArrayInputStream(query.getBytes());
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        Client client = new Client("localhost", PORT_NUMBER);
        client.run(bais, baos);

        String expected = "3" + sep + "fold true" + sep + "tfile1 false" + sep + "tfile2 false" + sep + sep;

        assertEquals(expected, baos.toString());
    }

    @Test
    public void testGetEmptyList() {
        String query = "1 " + folder.getRoot().getPath() + sep + "e";
        ByteArrayInputStream bais = new ByteArrayInputStream(query.getBytes());
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        Client client = new Client("localhost", PORT_NUMBER);
        client.run(bais, baos);

        String expected = "0" + sep + sep;

        assertEquals(expected, baos.toString());
    }

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

        String query = "1 " + folder.getRoot() + sep + "e";
        ByteArrayInputStream bais = new ByteArrayInputStream(query.getBytes());
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        Client client = new Client("localhost", PORT_NUMBER);
        client.run(bais, baos);

        String expected = "7" + sep + "f1 false" + sep + "f2 false" + sep + "finf false" + sep
                + "k1 true" + sep + "k2 true" + sep + "k3 true" + sep
                + "the file false" + sep + sep;

        assertEquals(expected, baos.toString());
    }

    @Test
    public void testGetFile() throws IOException {
        File f = folder.newFile("file");
        PrintWriter pw = new PrintWriter(f);
        String content = "Paris, 1923. Laying penguin, burning house. ";
        pw.print(content);

        String query = "2 " + f.getPath() + sep + "e";
        ByteArrayInputStream bais = new ByteArrayInputStream(query.getBytes());
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        Client client = new Client("localhost", PORT_NUMBER);
        client.run(bais, baos);

        String actualPath = "downloads/file".replace("/", File.separator);
        File actual = new File(actualPath);
        assertTrue(FileUtils.contentEquals(f, actual));
    }

    @Test
    public void testGetBigFile() throws IOException {
        String file = "src/test/resources/BigFile".replace("/", File.separator);
        String query = "2 " + file + sep + "e";
        ByteArrayInputStream bais = new ByteArrayInputStream(query.getBytes());
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        Client client = new Client("localhost", PORT_NUMBER);
        client.run(bais, baos);

        File original = new File(file);
        String actualPath = "downloads/BigFile".replace("/", File.separator);
        File actual = new File(actualPath);
        assertTrue(FileUtils.contentEquals(original, actual));
    }
}