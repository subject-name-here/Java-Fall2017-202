package ru.spbau.mit.java.paradov;

public class ClientMain {
    public static void main(String[] args) {
        if (args.length < 2) {
            throw new NotEnoughArgumentsException("Expected at least two arguments: host name & port number.");
        }

        String hostName = args[0];
        int portNumber = Integer.parseInt(args[1]);

        Client client = new Client(hostName, portNumber);
        client.run(System.in, System.out);
    }
}
