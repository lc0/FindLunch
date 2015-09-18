package com.brainscode.nearcommunication;

/**
 * Created by platerosanchezm on 18/09/15.
 */
public class Bros {
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPosition() {
        return position;
    }

    public boolean isUpforLunch() {
        return upforLunch;
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

    @Override
    public boolean equals(Object o) {
        if(o instanceof Bros) {
            if (((Bros) o).getId() != this.id){
                return false;
            }
        }
        return super.equals(o);
    }

    boolean upforLunch;
}
