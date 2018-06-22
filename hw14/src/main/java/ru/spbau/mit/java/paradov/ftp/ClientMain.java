package ru.spbau.mit.java.paradov.ftp;

/**
 * Class that launches work with client.
 */
public class ClientMain {
    /**
     * Starts client that connecnts to given host name and port number
     * @param args args for client launch: host name and port number
     */
    public static void main(String[] args) {
        if (args.length < 2) {
            System.err.println("Error: expected at least two arguments: host name & port number.");
            return;
        }

        String hostName = args[0];
        int portNumber = Integer.parseInt(args[1]);

        Client client = new Client(hostName, portNumber);
        client.run(System.in, System.out);
    }
}
