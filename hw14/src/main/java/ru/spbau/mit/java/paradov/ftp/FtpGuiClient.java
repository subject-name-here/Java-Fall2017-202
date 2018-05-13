package ru.spbau.mit.java.paradov.ftp;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Class that implements ftp for server, implemented in server class. It can send queries of 2 types.
 * It is a cut version of Client from hw13, because it only needs to query server.
 */
public class FtpGuiClient {
    /** Name of host we connect to. */
    private String hostName;
    /** Port number that server listens to. */
    private int portNumber;
    /** Size of buffer when reading something.  */
    private static final int BUF_SIZE = 1024;

    public static Integer DEFAULT_PORT = 22229;

    /**
     * Constructor that initializes fileds.
     * @param hostName name of host
     * @param portNumber port number
     */
    public FtpGuiClient(String hostName, int portNumber) {
        this.hostName = hostName;
        this.portNumber = portNumber;
    }

    /**
     * Sends query to server, then process its answer.
     * @param type type of query (1 or 2)
     * @param path path to file we need in query
     * @throws IOException if there is a problem with reading or writing from/to socket
     */
    public String[] sendQuery(int type, String path) throws IOException {
        try (Socket s = new Socket(hostName, portNumber);
             DataInputStream dis = new DataInputStream(s.getInputStream());
             DataOutputStream dos = new DataOutputStream(s.getOutputStream())) {

            dos.writeInt(type);
            dos.write(path.getBytes());
            dos.flush();
            s.shutdownOutput();

            if (type == 1) {
                return getList(dis);
            } else {
                saveFile(dis, path);
                return null;
            }

        } catch (UnknownHostException e) {
            System.err.println("Host with this name doesn't exist.");
            throw e;
        }
    }

    /**
     * Processing of answer on query 1: receiving files list and orinting it to given PrintWriter.
     * @param dis DataInputStream where we receive data from
     * @throws IOException if there is a problem with reading from socket or writing to given writer
     */
    private String[] getList(DataInputStream dis) throws IOException {
        int size = dis.readInt();

        StringBuilder list = new StringBuilder();
        byte[] buf = new byte[BUF_SIZE];
        int readSymbols;
        while ((readSymbols = dis.read(buf)) != -1) {
            list.append(new String(buf, 0, readSymbols));
        }

        return list.toString().split("\n");
    }

    /**
     * Saves file, content of which is translated through given DataInputStream, to directory "downloads".
     * @param dis DataInputStream where content of our file read from
     * @param filename name of file we want to get
     * @throws IOException if there is a problem with reading from socket or saving file
     */
    private void saveFile(DataInputStream dis, String filename) throws IOException {
        addDownloadsDirectory();

        File tmp = new File(filename);
        filename = "downloads" + File.separator + tmp.getName();
        long size = dis.readLong();

        byte[] buf = new byte[BUF_SIZE];
        int readSymbols;

        try (DataOutputStream dos = new DataOutputStream(new FileOutputStream(new File(filename)))) {
            while ((readSymbols = dis.read(buf)) != -1) {
                dos.write(buf, 0, readSymbols);
            }
        } catch (FileNotFoundException e) {
            System.err.println("Failed to create file: ");
            System.err.println(e.getMessage());
        }
    }

    /**
     * Util function: creates directory, where all downloaded files will be saved.
     */
    private void addDownloadsDirectory() {
        File dir = new File("downloads");
        dir.mkdir();
    }
}
