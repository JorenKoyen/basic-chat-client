package be.kdg.chat.client;

import be.kdg.chat.TextReceiver;

public interface IChatClient {
    void send(String message);
    void receive(String message);
    void register();
    void unregister();

    void setTextReceiver(TextReceiver textReceiver);
    String getName();

}
