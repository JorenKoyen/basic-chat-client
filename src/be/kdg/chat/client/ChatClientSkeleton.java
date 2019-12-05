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

            // wait sync for request
            MethodCallMessage request = messageManager.receiveSync();

            // handle request
            this.handleRequest(request);
        }
    }

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
            case "getName":
                this.handleGetName(request);
                break;
            default:
                LOGGER.error("Unrecognized request " + request.getMethod() + " received");

        }
    }
    private void ack(NetworkAddress to) {
        MethodCallMessage ack = new MethodCallMessage(this.messageManager.getAddress(), "ack");
        ack.addParameter("result", "ok");
        this.messageManager.send(ack, to);
    }

    // == DELEGATIONS =======================waitForAck
    private void handleReceive(MethodCallMessage methodCallMessage) {
        String message = methodCallMessage.getParameter("message");
        this.client.receive(message);

        // request finished -> ack
        ack(methodCallMessage.getOrigin());
    }
    private void handleGetName(MethodCallMessage methodCallMessage) {
        // get correct name from client
        String name = this.client.getName();

        // send response to server
        MethodCallMessage response = new MethodCallMessage(this.messageManager.getAddress(), methodCallMessage.getMethod());
        response.addParameter("value", name);
        this.messageManager.send(response, methodCallMessage.getOrigin());
    }

}
