package be.kdg.chat;

import be.kdg.chat.communication.MessageManager;
import be.kdg.chat.communication.MethodCallMessage;
import be.kdg.chat.communication.NetworkAddress;

public class ChatServerStub implements IChatServer {
    private final NetworkAddress remoteAddress;
    private final MessageManager messageManager;

    public ChatServerStub(NetworkAddress address) {
        this.remoteAddress = address;
        this.messageManager = new MessageManager();
    }

    @Override
    public void register(IChatClient client) {
        MethodCallMessage message = new MethodCallMessage(messageManager.getAddress(), "register");
        messageManager.send(message, this.remoteAddress);
    }

    @Override
    public void unregister(IChatClient client) {
        MethodCallMessage message = new MethodCallMessage(messageManager.getAddress(), "unregister");
        messageManager.send(message, this.remoteAddress);
    }

    @Override
    public void send(String name, String message) {
        MethodCallMessage methodCall = new MethodCallMessage(messageManager.getAddress(), "send");
        methodCall.addParameter("name", name);
        methodCall.addParameter("message", message);
        messageManager.send(methodCall, this.remoteAddress);
    }

    public MessageManager getMessageManager() {
        return messageManager;
    }
}
