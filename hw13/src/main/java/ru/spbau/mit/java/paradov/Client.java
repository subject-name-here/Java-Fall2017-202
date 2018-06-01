package ru.spbau.mit.java.paradov;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

/**
 * Class that implements client for server, implemented in Server class. It can send queries of 2 types,
 * show what it can do and stop its work.
 */
public class Client {
    /** Name of host we connect to. */
    private String hostName;
    /** Port number that server listens to. */
    private int portNumber;
    /** Size of buffer when reading something.  */
    private static final int BUF_SIZE = 1024;

    /**
     * Constructor that initializes fields.
     * @param hostName name of host
     * @param portNumber port number
     */
    public Client(String hostName, int portNumber) {
        this.hostName = hostName;
        this.portNumber = portNumber;
    }

    /**
     * Main function of client that reads command from given input stream and gets the answer, then
     * process it correctly.
     * @param in input stream where we receive commands from
     * @param out output stream where we print everything
     */
    public void run(InputStream in, OutputStream out) {
        Scanner sc = new Scanner(in);
        PrintWriter pw = new PrintWriter(out, true);

        while (true) {
            //pw.println("Write command (h for help, e for exit).");

            String type = sc.next();
            if (type.equals("h")) {
                pw.println("h - get help (this page).");
                pw.println("e - stop client (exit).");
                pw.println("1 <path: String> - query server to list files in given directory.");
                pw.println("2 <path: String> - query server to return file by given path.");
            } else if (type.equals("1") || type.equals("2")) {
                if (sc.hasNext()) {
                    String path = sc.nextLine().trim();
                    try {
                        sendQuery(Integer.parseInt(type), path, pw);
                    } catch (IOException e) {
                        System.err.println("Error when tried to read from/write to socket: ");
                        System.err.println(e.getMessage());
                    }
                } else {
                    pw.println("Expected argument, but given none!");
                }
            } else if (type.equals("e")) {
                break;
            } else {
                pw.println("Unknown command. Print \"h\" for help");
            }

        }
    }

    /**
     * Sends query to server, then process its answer.
     * @param type type of query (1 or 2)
     * @param path path to file we need in query
     * @param pw PrintWriter where we will write file list (when type == 1)
     * @throws IOException if there is a problem with reading or writing from/to socket
     */
    private void sendQuery(int type, String path, PrintWriter pw) throws IOException {
        try (Socket s = new Socket(hostName, portNumber);
             DataInputStream dis = new DataInputStream(s.getInputStream());
             DataOutputStream dos = new DataOutputStream(s.getOutputStream())) {

            dos.writeInt(type);
            dos.write(path.getBytes());
            dos.flush();
            s.shutdownOutput();

            if (type == QueryType.LIST_FILES.getValue()) {
                getList(dis, pw);
            } else if (type == QueryType.DOWNLOAD_FILE.getValue()) {
                if (saveFile(dis, path)) {
                    pw.println("File was successfully saved!");
                } else {
                    pw.println("Couldn't save file!");
                }
            }

        } catch (UnknownHostException e) {
            System.err.println("Host with this name doesn't exist.");
        }
    }

    /**
     * Processing of answer on query 1: receiving files list and orinting it to given PrintWriter.
     * @param dis DataInputStream where we receive data from
     * @param pw writer where all info will be printed
     * @throws IOException if there is a problem with reading from socket or writing to given writer
     */
    private void getList(DataInputStream dis, PrintWriter pw) throws IOException {
        int size = dis.readInt();

        StringBuilder list = new StringBuilder();
        byte[] buf = new byte[BUF_SIZE];
        int readSymbols;
        while ((readSymbols = dis.read(buf)) != -1) {
            list.append(new String(buf, 0, readSymbols));
        }

        pw.println(size);
        pw.println(list.toString());
    }

    /**
     * Saves file, content of which is translated through given DataInputStream, to directory "downloads".
     * @param dis DataInputStream where content of our file read from
     * @param filename name of file we want to get
     * @throws IOException if there is a problem with reading from socket or saving file
     */
    private boolean saveFile(DataInputStream dis, String filename) throws IOException {
        long size = dis.readLong();

        if (size == -1) {
            return false;
        }

        addDownloadsDirectory();

        File tmp = new File(filename);
        filename = "downloads" + File.separator + tmp.getName();

        byte[] buf = new byte[BUF_SIZE];
        int readSymbols;

        try (DataOutputStream dos = new DataOutputStream(new FileOutputStream(new File(filename)))) {
            while ((readSymbols = dis.read(buf)) != -1) {
                dos.write(buf, 0, readSymbols);
            }

            return true;
        } catch (FileNotFoundException e) {
            System.err.println("Failed to create file: ");
            System.err.println(e.getMessage());
            return false;
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
