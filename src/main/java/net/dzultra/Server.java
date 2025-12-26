package net.dzultra;

import net.dzultra.ButtonInteractionListener.ServerHandler;

public class Server {
    private final String name;
    private final String channelId;
    private boolean running;
    private final ServerHandler handler;

    public Server(String name, String channelId, ServerHandler handler) {
        this.name = name;
        this.channelId = channelId;
        this.handler = handler;
        this.running = false;
    }

    public String getName() {
        return name;
    }

    public String getChannelId() {
        return channelId;
    }

    public boolean isRunning() {
        return running;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    public ServerHandler getHandler() {
        return handler;
    }
}
