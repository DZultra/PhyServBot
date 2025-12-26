package net.dzultra;

import java.util.HashMap;
import java.util.Map;

public class ServerRegistry {

    private static final Map<String, Server> SERVERS_BY_NAME = new HashMap<>();
    private static final Map<String, Server> SERVERS_BY_CHANNEL = new HashMap<>();

    static {
        registerServers(
                new Server(
                        "neoforge",
                        "1454211225276645611",
                        ButtonInteractionListener::handleNeoForgeServer
                )
        );
    }

    private static void registerServers(Server... servers) {
        for (Server server : servers) {
            SERVERS_BY_NAME.put(server.getName(), server);
            SERVERS_BY_CHANNEL.put(server.getChannelId(), server);
        }
    }

    public static Server getByChannelId(String channelId) {
        return SERVERS_BY_CHANNEL.get(channelId);
    }

    public static Server getByName(String name) {
        return SERVERS_BY_NAME.get(name);
    }

    public static boolean isServerRunning(String channelId) {
        Server server = SERVERS_BY_CHANNEL.get(channelId);
        return server != null && server.isRunning();
    }
}
