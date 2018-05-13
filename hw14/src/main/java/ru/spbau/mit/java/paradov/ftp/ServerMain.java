package ru.spbau.mit.java.paradov.ftp;

import java.util.Scanner;

/**
 * Class that implements work with server as a server master.
 */
public class ServerMain {
    /**
     * Runs server on given port. Also it waits until you write in command line word "stop",
     * so it will stop server and this method too.
     * @param args given args; first argument must be port number.
     * @throws NotEnoughArgumentsException if there is not enough args (at least one must be)
     */
    public static void main(String[] args) {
        if (args.length == 0) {
            throw new NotEnoughArgumentsException("Expected at least one argument: port number.");
        }

        int portNumber = Integer.parseInt(args[0]);

        Server server = new Server(portNumber);
        Thread serverThread = new Thread(server::run);
        serverThread.start();

        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.println("Write \"stop\" to stop server.");
            String line = sc.nextLine();
            if (line.equals("stop")) {
                serverThread.interrupt();
                break;
            }
        }
    }
}