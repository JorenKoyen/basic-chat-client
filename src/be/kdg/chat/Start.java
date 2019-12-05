package be.kdg.chat;

import be.kdg.chat.client.ChatClientImplementation;
import be.kdg.chat.client.ChatClientSkeleton;
import be.kdg.chat.client.IChatClient;
import be.kdg.chat.communication.NetworkAddress;
import be.kdg.chat.server.ChatServerStub;

import java.util.Scanner;

public class Start {

    public static void main(String[] args) {
        // check server connection details
        if (args.length != 2) {
            System.err.println("Usage: java Start <chat_server_ip> <chat_server_port>");
            System.exit(1);
        }

        // create server connection address
        int port = Integer.parseInt(args[1]);
        NetworkAddress serverAddress = new NetworkAddress(args[0], port);

        // create server stub
        ChatServerStub chatServerStub = new ChatServerStub(serverAddress);

        // create client skeleton for listening to incoming requests
        ChatClientSkeleton chatClientSkeleton = new ChatClientSkeleton();

        // create chat client
        ChatClientImplementation chatClient = new ChatClientImplementation(chatServerStub, "Joren");

        // register client in skeleton
        chatClientSkeleton.setClient(chatClient);

        // register listen address in chat server stub
        chatServerStub.setReceiveAddress(chatClientSkeleton.getNetworkAddress());

        // inject client into UI frame
        new ChatFrame(chatClient, chatClientSkeleton);

        // register to server
        chatClient.register();

        // TODO: run listener on different thread
        // start listening for incoming requests
        chatClientSkeleton.listen();


    }


}
