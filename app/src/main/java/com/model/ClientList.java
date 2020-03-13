package com.model;

public class ClientList {
    private int placeId;
    private String clientName;
    private String mno;
    private String clientPrice;
    private String clientHours;
    private boolean favorite = false;

    public ClientList(int placeId,String clientName, String mno, String clientPrice, String clientHours){
        this.placeId=placeId;
        this.clientName=clientName;
        this.mno=mno;
        this.clientPrice=clientPrice;
        this.clientHours=clientHours;
    }

    public int getPlaceId() {
        return placeId;
    }

    public String getClientName() {
        return clientName;
    }

    public String getMno() {
        return mno;
    }

    public String getClientHours() {
        return clientHours;
    }

    public String getClientPrice() {
        return clientPrice;
    }

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }
}
