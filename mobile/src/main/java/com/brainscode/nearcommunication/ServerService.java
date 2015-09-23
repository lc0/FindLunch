package com.brainscode.nearcommunication;

/**
 * Created by khomenkos on 24/09/15.
 */
public class ServerService {
    private String host;
    private int port;

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public ServerService(String host, int port) {
        this.host = host.substring(1);
        this.port = port;
    }
}
