package be.kdg.chat;


import be.kdg.chat.communication.MessageManager;
import be.kdg.chat.communication.MethodCallMessage;
import be.kdg.chat.communication.NetworkAddress;
import be.kdg.chat.util.Logger;

import java.net.ServerSocket;

public class ChatClientListener {
    private final MessageManager messageManager;
    private final IChatClient client;
    private final static Logger LOGGER = Logger.getLogger("Listener");

    public ChatClientListener(ServerSocket socket, IChatClient client) {
        this.messageManager = new MessageManager(socket);
        this.client = client;
    }

    // == PUBLIC METHODS ===================
    public void run() {
        LOGGER.info("Server started listening on " + this.messageManager.getAddress());

        // infinite loop waiting for requests
        while (true) {
            MethodCallMessage request = messageManager.receiveSync();
            handleRequest(request);
        }
    }

    // == MESSAGE CONTROLLERS ==============
    private void handleRequest(MethodCallMessage request) {
        LOGGER.info("Incoming request message | " + request);

        switch (request.getMethod()) {
            case "receive":
                this.handleReceive(request);
                break;
            default:
                LOGGER.error("Received an unknown request");
        }

    }


    // == PRIVATE METHODS ==================
    private void handleReceive(MethodCallMessage request) {
        // send message to client instance
        String message = request.getParameter("message");
        this.client.receive(message);
    }

}
