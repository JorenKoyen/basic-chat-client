package be.kdg.chat.communication;

public class NetworkAddress {
    private final String address;
    private final int port;

    public NetworkAddress(String address, int port) {
        this.address = address;
        this.port = port;
    }

    public String getAddress() {
        return address;
    }

    public int getPort() {
        return port;
    }

    @Override
    public String toString() {
        return this.address + ":" + this.port;
    }
}
