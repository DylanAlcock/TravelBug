package com.app.travelbug.data.model;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class ClusterMarker implements ClusterItem {
    private final LatLng mPosition;
    private final String mTitle;
    private final String mSnippet;
    private int iconPicture;

    public ClusterMarker(double lat, double lng, String title, String snippet) {
        mPosition = new LatLng(lat, lng);
        mTitle = title;
        mSnippet = snippet;
    }

    public ClusterMarker(LatLng mPosition, String mTitle, String mSnippet, int iconPicture) {
        this.mPosition = mPosition;
        this.mTitle = mTitle;
        this.mSnippet = mSnippet;
        this.iconPicture = iconPicture;
    }

    public LatLng getPosition() {
        return mPosition;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getSnippet() {
        return mSnippet;
    }

    public int getIconPicture() {
        return iconPicture;
    }

    public void setIconPicture(int iconPicture) {
        this.iconPicture = iconPicture;
    }

}
