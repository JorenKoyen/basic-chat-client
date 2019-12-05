package be.kdg.chat.client;

import be.kdg.chat.server.IChatServer;
import be.kdg.chat.TextReceiver;
import be.kdg.chat.util.Logger;

public class ChatClientImplementation implements IChatClient {
    private final static Logger LOGGER = Logger.getLogger("chat-client");
    private final IChatServer server;
    private final String name;
    private TextReceiver textReceiver;

    // == CONSTRUCTOR ======================
    public ChatClientImplementation(IChatServer server, String name) {
        this.server = server;
        this.name = name;
    }


    // == INTERFACE METHODS ==============
    @Override
    public void send(String message) {
        this.server.send(this.name, message);
    }

    @Override
    public void receive(String message) {
        if (this.textReceiver == null) {
            LOGGER.error("Unable to find viable text receiver");
            return; // stop method
        }

        this.textReceiver.receive(message);
    }

    @Override
    public void register() {
        this.server.register(this);
    }

    @Override
    public void unregister() {
        this.server.unregister(this);
    }

    @Override
    public void setTextReceiver(TextReceiver textReceiver) {
        this.textReceiver = textReceiver;
    }

    @Override
    public String getName() {
        return this.name;
    }
}
