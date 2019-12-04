package be.kdg.chat;

public class ChatClient implements IChatClient {
    private TextReceiver textReceiver;
    private String name;
    private IChatServer chatServer;

    public ChatClient(IChatServer chatServer, String name) {
        this.chatServer = chatServer;
        this.name = name;
    }

    // == GETTER ===========================
    public String getName() {
        return name;
    }

    // == SETTER ===========================
    public void setTextReceiver(TextReceiver textReceiver) {
        this.textReceiver = textReceiver;
    }

    // == METHODS ==========================
    @Override
    public void send(String message) {
        this.chatServer.send(this.name, message);
    }

    @Override
    public void receive(String message) {
        if (this.textReceiver == null) return;
        this.textReceiver.receive(message);
    }

    @Override
    public void register() {
        this.chatServer.register(this);
    }

    @Override
    public void unregister() {
        this.chatServer.unregister(this);
    }
}
