package com.brainscode.nearcommunication;

import android.util.Log;

/**
 * Created by platerosanchezm on 18/09/15.
 */
public class Bros {
    @Override
    public String toString() {
        return "Bros{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", position='" + position + '\'' +
                ", upforLunch=" + upforLunch +
                '}';
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

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

    @Override
    public boolean equals(Object o) {

        if(o instanceof Bros) {
            if (this.id.equals(((Bros) o).getId())){
                return true;
            }
            if (this.name.equals(((Bros) o).getName())){
                return true;
            }
        }

        return false;
    }
}
