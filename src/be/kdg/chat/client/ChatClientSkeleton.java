package be.kdg.chat.client;

import be.kdg.chat.communication.MessageManager;
import be.kdg.chat.communication.MethodCallMessage;
import be.kdg.chat.communication.NetworkAddress;
import be.kdg.chat.util.Logger;

public class ChatClientSkeleton {
    private final static Logger LOGGER = Logger.getLogger("chat-listener");
    private final NetworkAddress networkAddress;
    private final MessageManager messageManager;
    private IChatClient client;
    private boolean isListening = false;

    // == CONSTRUCTOR ======================
    public ChatClientSkeleton() {
        this.messageManager = new MessageManager();
        this.networkAddress = this.messageManager.getAddress();
    }

    // == LISTENER =========================
    public void listen() {
        // check if client has been set
        if (this.client == null) {
            LOGGER.error("Unable to start listening to incoming request if no client has been set");
            System.exit(1);
        }

        LOGGER.info("Started listening on " + this.networkAddress);

        this.isListening = true;
        while (isListening) {
            MethodCallMessage request = messageManager.receiveSync();
            this.handleRequest(request);
        }
    }

    // TODO: use method
    public void stopListening() {
        if (!this.isListening) {
            LOGGER.error("Unable to stop listening, currently listening");
            return;
        }

        this.isListening = false;
    }

    // == SETTERS ==========================
    public void setClient(IChatClient client) {
        this.client = client;
    }

    // == GETTERS ==========================
    public NetworkAddress getNetworkAddress() {
        return this.networkAddress;
    }

    // == MESSAGE CONTROLLER ===============
    private void handleRequest(MethodCallMessage request) {
        LOGGER.info("Received request | " + request);

        switch (request.getMethod()) {

            case "receive":
                this.handleReceive(request);
                break;
            default:
                LOGGER.error("Unrecognized request " + request.getMethod() + " received");

        }
    }

    // == DELEGATIONS =======================
    private void handleReceive(MethodCallMessage methodCallMessage) {
        String message = methodCallMessage.getParameter("message");
        this.client.receive(message);
    }

}
