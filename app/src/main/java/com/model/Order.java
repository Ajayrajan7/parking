package com.model;

public class Order {
    private int placeId;
    private String placeName;
    private String location;
    private String placePrice;
    private String placeHours;
    private boolean favorite = false;

    public Order(int placeId, String placeName, String location, String placePrice, String placeHours) {
        this.placeId = placeId;
        this.placeName = placeName;
        this.location = location;
        this.placePrice = placePrice;
        this.placeHours= placeHours;
    }

    public int getPlaceId() {
        return placeId;
    }

    public String getLocation() {
        return location;
    }

    public String getPlaceHours() {
        return placeHours;
    }

    public String getPlaceName() {
        return placeName;
    }

    public String getPlacePrice() {
        return placePrice;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    public boolean isFavorite() {
        return favorite;
    }
}
