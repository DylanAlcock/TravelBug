package com.app.travelbug.data.model;

import com.google.android.gms.maps.model.LatLng;

public class PlaceInfo {

    private int placeID;
    private String title;
    private String snippet;
    private LatLng latLng;
    private String website;
    private String address;
    private String price;

    public PlaceInfo(int placeID, String title, String snippet, LatLng latLng,
                     String website, String address, String price) {

        this.placeID = placeID;
        this.title = title;
        this.snippet = snippet;
        this.latLng = latLng;
        this.website = website;
        this.address = address;
        this.price = price;
    }

    public int getPlaceID() {
        return placeID;
    }

    public void setPlaceID(int placeID) {
        this.placeID = placeID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSnippet() {
        return snippet;
    }

    public void setSnippet(String snippet) {
        this.snippet = snippet;
    }

    public LatLng getLatLng() {
        return latLng;
    }

    public double getLat() {
        return latLng.latitude;
    }

    public double getLng() {
        return latLng.longitude;
    }

    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}

