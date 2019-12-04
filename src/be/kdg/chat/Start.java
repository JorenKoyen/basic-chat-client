package be.kdg.chat;

import be.kdg.chat.communication.NetworkAddress;

import java.util.Scanner;

public class Start {

    public static void main(String[] args) {
        // check server connection details
        if (args.length != 2) {
            System.err.println("Usage: java Start <chat_server_ip> <chat_server_port>");
            System.exit(1);
        }

        // create address object
        int port = Integer.parseInt(args[1]);
        NetworkAddress address = new NetworkAddress(args[0], port);

        Scanner scanner = new Scanner(System.in);

        // ask chat client name
        System.out.print("Username: ");
        String name = scanner.nextLine();

        // check if name isn't empty
        if (name.isEmpty()) {
            System.err.println("Please enter a valid name.");
            System.exit(1);
        }


        // setup server stub
        ChatServerStub serverStub = new ChatServerStub(address);

        // create client
        ChatClient client = new ChatClient(serverStub, name);

        // inject client into UI frame
        new ChatFrame(client);

        // register client with server
        client.register();

        // start listening to incoming messages from other clients
        ChatClientListener listener = new ChatClientListener(serverStub.getMessageManager().getSocket(), client);
        listener.run();

    }


}
