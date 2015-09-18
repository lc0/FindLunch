package com.brainscode.nearcommunication;

/**
 * Created by platerosanchezm on 18/09/15.
 */
public class Bros {
    String id;
    String name;

    String position;

    public Bros(String name, String position, boolean upforLunch, String id) {
        this.name = name;
        this.position = position;
        this.upforLunch = upforLunch;
        this.id = id;
    }

    public Bros(String name, String position, boolean upforLunch) {
        this.name = name;
        this.position = position;
        this.upforLunch = upforLunch;
    }


    boolean upforLunch;
}
