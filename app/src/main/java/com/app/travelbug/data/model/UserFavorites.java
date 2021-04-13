package com.app.travelbug.data.model;

import java.util.ArrayList;

public class UserFavorites {

    private String userId;
    private ArrayList<Integer> favoritePlacesIds;

    public UserFavorites(String userId, ArrayList<Integer> favoritePlacesIds) {
        this.userId = userId;
        this.favoritePlacesIds = favoritePlacesIds;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public ArrayList<Integer> getFavoritePlacesIds() {
        return favoritePlacesIds;
    }

    public void setFavoritePlacesIds(ArrayList<Integer> favoritePlacesIds) {
        this.favoritePlacesIds = favoritePlacesIds;
    }
}
