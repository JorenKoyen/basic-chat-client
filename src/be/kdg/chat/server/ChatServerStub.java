package be.kdg.chat.server;

import be.kdg.chat.client.IChatClient;
import be.kdg.chat.communication.MessageManager;
import be.kdg.chat.communication.MethodCallMessage;
import be.kdg.chat.communication.NetworkAddress;
import be.kdg.chat.server.IChatServer;
import be.kdg.chat.util.Logger;

public class ChatServerStub implements IChatServer {
    private final static Logger LOGGER = Logger.getLogger("server-stub");
    private final MessageManager messageManager;
    private final NetworkAddress serverAddress;
    private NetworkAddress receiveAddress;

    public ChatServerStub(NetworkAddress serverAddress) {
        this.serverAddress = serverAddress;
        this.messageManager = new MessageManager();
    }

    // == PRIVATE METHODS ==================
    private void waitForAck() {

        // infinite loop waiting for ack
        while (true) {

            // wait sync for ack
            MethodCallMessage ack = messageManager.receiveSync();

            // check if indeed ack
            if (ack.getMethod().equals("ack")
                    && ack.getParameter("result").equals("ok")) {
                LOGGER.info("Acknowledgement received");
                return;
            }

        }

    }

    // == INTERFACE METHODS ================
    @Override
    public void register(IChatClient client) {
        if (this.receiveAddress == null) {
            LOGGER.error("No receive address has been set, unable to register to server");
            return;
        }

        // send registration to server
        MethodCallMessage message = new MethodCallMessage(messageManager.getAddress(), "register");
        message.addParameter("receive.address", receiveAddress.getAddress());
        message.addParameter("receive.port", String.valueOf(receiveAddress.getPort()));
        messageManager.send(message, this.serverAddress);

        waitForAck();
    }

    @Override
    public void unregister(IChatClient client) {
        MethodCallMessage message = new MethodCallMessage(messageManager.getAddress(), "unregister");
        messageManager.send(message, this.serverAddress);

        waitForAck();
    }

    @Override
    public void send(String name, String message) {
        MethodCallMessage methodCall = new MethodCallMessage(messageManager.getAddress(), "send");
        methodCall.addParameter("name", name);
        methodCall.addParameter("message", message);
        messageManager.send(methodCall, this.serverAddress);

        waitForAck();
    }

    // == SETTER ===========================
    public void setReceiveAddress(NetworkAddress receiveAddress) {
        this.receiveAddress = receiveAddress;
    }
}
