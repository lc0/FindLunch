package com.brainscode.nearcommunication;

/**
 * Created by platerosanchezm on 18/09/15.
 */
public class Bros {
    @Override
    public String toString() {
        return "Bros{" +
                "host='" + host + '\'' +
                ", name='" + name + '\'' +
                ", position='" + position + '\'' +
                ", upforLunch=" + upforLunch +
                '}';
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public String getName() {
        return name;

    }

    String host;
    int port;
    String name;

    String position;

    public Bros(String name, String position, boolean upforLunch, String host, int port) {
        this.name = name;
        this.position = position;
        this.upforLunch = upforLunch;
        this.host = host;
        this.port = port;
    }

    public Bros(String name, String position, boolean upforLunch) {
        this.name = name;
        this.position = position;
        this.upforLunch = upforLunch;
    }


    boolean upforLunch;

    @Override
    public boolean equals(Object o) {

        if(o instanceof Bros) {
            if (this.host.equals(((Bros) o).getHost())){
                return true;
            }
            // Currently name is not unique, because we use the same service name
//            if (this.name.equals(((Bros) o).getName())){
//                return true;
//            }
        }

        return false;
    }
}
