package streams.handler;

public interface EventHandler {
    void processEvent(byte[] rawMessage);
}