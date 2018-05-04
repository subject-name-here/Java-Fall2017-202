package ru.spbau.mit.java.paradov;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Client {
    private String hostName;
    private int portNumber;
    private static final int BUF_SIZE = 1024;

    public Client(String hostName, int portNumber) {
        this.hostName = hostName;
        this.portNumber = portNumber;
    }

    public void run(InputStream in, OutputStream out) {
        Scanner sc = new Scanner(in);
        PrintWriter pw = new PrintWriter(out, true);

        while (true) {
            pw.println("Write command (h for help, e for exit).");

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
            }

        }
    }

    private void sendQuery(int type, String path, PrintWriter pw) throws IOException {
        try (Socket s = new Socket(hostName, portNumber);
             DataInputStream dis = new DataInputStream(s.getInputStream());
             DataOutputStream dos = new DataOutputStream(s.getOutputStream())) {

            dos.writeInt(type);
            dos.write(path.getBytes());
            dos.flush();
            s.shutdownOutput();

            if (type == 1) {
                getList(dis, pw);
            } else {
                saveFile(dis, path);
            }

        } catch (UnknownHostException e) {
            System.err.println("Host with this name doesn't exist.");
        }
    }

    private void saveFile(DataInputStream dis, String filename) throws IOException {
        filename = "downloads" + File.pathSeparator + filename;
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

}
