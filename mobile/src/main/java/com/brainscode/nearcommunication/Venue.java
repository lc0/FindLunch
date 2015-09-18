package com.brainscode.nearcommunication;

/**
 * Created by khomenkos on 18/09/15.
 */
public class Venue {
    String name;
    String category;
    private String categoryIcon;

    int distance;
    double lat;
    double lng;

    public Venue(String name, String category, String categoryIcon, int distance, double lat, double lng) {
        this.name = name;
        this.category = category;
        this.setCategoryIcon(categoryIcon);
        this.distance = distance;
        this.lat = lat;
        this.lng = lng;
    }

    public String getCategoryIcon() {
        return categoryIcon;
    }

    public void setCategoryIcon(String categoryIcon) {
        this.categoryIcon = categoryIcon;
    }
}
