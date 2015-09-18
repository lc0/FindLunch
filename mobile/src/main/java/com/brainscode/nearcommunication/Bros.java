package com.brainscode.nearcommunication;

import android.graphics.drawable.Drawable;

/**
 * Created by platerosanchezm on 18/09/15.
 */
public class Bros {

    Drawable pic;
    String name;

    String position;

    public Bros(String name, String position, boolean upforLunch) {
        this.name = name;
        this.position = position;
        this.upforLunch = upforLunch;
    }

    boolean upforLunch;
}
