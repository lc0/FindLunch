package com.brainscode.nearcommunication;

/**
 * Created by khomenkos on 18/09/15.
 */
public class Venue {
    String name;
    String category;
    String categoryIcon;

    int distance;
    double lat;
    double lng;

    public Venue(String name, String category, String categoryIcon, int distance, double lat, double lng) {
        this.name = name;
        this.category = category;
        this.categoryIcon = categoryIcon;
        this.distance = distance;
        this.lat = lat;
        this.lng = lng;
    }
}
