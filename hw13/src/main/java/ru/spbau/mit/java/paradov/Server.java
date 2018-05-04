package ru.spbau.mit.java.paradov;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Class that implements server. Allows to query list files by given way and download file to
 * directory "downloads/".
 */
public class Server {
    /** Server socket. */
    private ServerSocket serverSocket;
    /** Thread pool where all query processing is stacked. */
    private ExecutorService pool;

    /** Max time for processing one query. */
    private static final int MAX_TASK_TIME = 10;
    /** Size of buffer when reading through socket. */
    private static final int BUF_SIZE = 1024;

    /**
     * Constructor of this class. Creates server socket and sets timeout of listening
     * (so we can stop some time). Also initializes thread pool.
     * @param portNumber number of port where server socket is created
     */
    public Server(int portNumber) {
        try {
            serverSocket = new ServerSocket(portNumber);
            serverSocket.setSoTimeout(5000);
        } catch (IOException e) {
            System.err.println("Error when tried to create a server: ");
            System.err.println(e.getMessage());
            System.err.println("Emergency stop.");
            System.exit(1);
        }

        pool = Executors.newFixedThreadPool(10);
    }

    /**
     * Runs the server. Until thread where server is started isn't interrupted, it listens its socket.
     * If someone connects, adds to pool processing of query.
     */
    public void run() {
        while (!Thread.interrupted()) {
            try {
                Socket s = serverSocket.accept();
                pool.submit(new SocketTask(s), MAX_TASK_TIME);
            } catch (SocketTimeoutException e) {
                // Nothing to do here, because it's planned.
            } catch (IOException e) {
                System.err.println("Error occurred when tried to listen a socket: ");
                System.err.println(e.getMessage());
            }
        }

        pool.shutdownNow();
    }

    /**
     * Class for task for thread pool of processing received query.
     */
    private class SocketTask implements Runnable {
        /**
         * Socket where all communication with client goes.
         */
        private Socket s;

        /**
         * Constructor that initializes field of socket
         * @param s socket
         */
        public SocketTask(Socket s) {
            this.s = s;
        }

        /**
         * Main function of processing query. Reads what query did send to it, then decides, what query
         * it is and process it.
         */
        @Override
        public void run() {
            try (DataInputStream dis = new DataInputStream(s.getInputStream());
                 DataOutputStream dos = new DataOutputStream(s.getOutputStream())) {
                int type = dis.readInt();

                String path = getPath(dis);

                if (type == 1) {
                    File dir = new File(path);
                    if (dir.isDirectory()) {
                        dos.writeInt(dir.listFiles().length);
                        dos.write(getFilesList(dir).getBytes());
                    } else {
                        dos.writeInt(0);
                    }
                } else if (type == 2) {
                    returnFile(path, dos);
                } else {
                    dos.write("Unknown operation type.".getBytes());
                }

                dos.flush();
            } catch (IOException e) {
                System.err.println("Error when tried to read/write data from/to socket:");
                System.err.println(e.getMessage());
            }

            try {
                s.close();
            } catch (IOException e) {
                System.err.println("Error when tried to close the socket:");
                System.err.println(e.getMessage());
            }
        }
    }

    /**
     * Util function to read path from DataInputStream.
     * @param dis DataInputStream, where we read path from
     * @return string representation of path
     * @throws IOException if there is a problem with reading from input stream
     */
    private String getPath(DataInputStream dis) throws IOException {
        StringBuilder path = new StringBuilder();
        byte[] buf = new byte[BUF_SIZE];
        int readSymbols;
        while ((readSymbols = dis.read(buf)) != -1) {
            path.append(new String(buf, 0, readSymbols));
        }

        return path.toString();
    }


    /**
     * Util function for processing query of type 1: get files list from given path.
     * @param dir path where directory expected to list files
     * @return string in format "N\n(filename isDir\n)*", where N is number of files in given directory,
     * filename is name of some file in directory, isDir - flag, detecting if this file is a directory
     */
    private String getFilesList(File dir) {
        if (!dir.isDirectory()) {
            return "";
        }

        StringBuilder result = new StringBuilder();
        File[] files = dir.listFiles();
        Arrays.sort(files);
        for (File f : files) {
            result.append(f.getName()).append(' ').append(f.isDirectory()).append('\n');
        }

        return result.toString();
    }

    /**
     * Util function for processing query of type 2: return the content of file.
     * @param path path to selected file
     * @param dos DataOutputStream, where we print file content to
     * @throws IOException if there is a problem with writing to output stream
     */
    private void returnFile(String path, DataOutputStream dos) throws IOException {
        File f = new File(path);
        if (!f.isFile()) {
            dos.write("0".getBytes());
            return;
        }

        dos.writeLong(f.length());
        try (DataInputStream fileDis = new DataInputStream(new FileInputStream(f))) {
            byte[] buf = new byte[BUF_SIZE];
            int readSymbols;
            while ((readSymbols = fileDis.read(buf)) != -1) {
                dos.write(buf, 0, readSymbols);
            }
        }
    }
}
