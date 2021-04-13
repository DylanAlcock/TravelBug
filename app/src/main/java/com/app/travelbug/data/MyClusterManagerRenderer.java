package com.app.travelbug.data;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Icon;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.app.travelbug.PlannerActivity;
import com.app.travelbug.ProfileActivity;
import com.app.travelbug.R;
import com.app.travelbug.data.model.ClusterMarker;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;
import com.google.maps.android.ui.IconGenerator;

import androidx.annotation.NonNull;

public class MyClusterManagerRenderer extends DefaultClusterRenderer<ClusterMarker>{
    private final IconGenerator iconGenerator;
    private final ImageView imageView;
    private final int markerWidth;
    private final int markerHeight;
    private final Context mContext;

    public MyClusterManagerRenderer(Context context, GoogleMap map, ClusterManager<ClusterMarker>
            clusterManager) {
        super(context, map, clusterManager);

        mContext = context;
        iconGenerator = new IconGenerator(context.getApplicationContext());
        imageView = new ImageView(context.getApplicationContext());
        markerWidth = (int) context.getResources().getDimension(R.dimen.custom_marker_image);
        markerHeight = (int) context.getResources().getDimension(R.dimen.custom_marker_image);
        imageView.setLayoutParams(new ViewGroup.LayoutParams(markerWidth, markerHeight));
        int padding = (int) context.getResources().getDimension(R.dimen.custom_marker_padding);
        imageView.setPadding(padding, padding, padding, padding);
        iconGenerator.setContentView(imageView);
    }

    @Override
    protected void onBeforeClusterItemRendered(@NonNull ClusterMarker item, @NonNull MarkerOptions markerOptions) {
        imageView.setImageResource(item.getIconPicture());
        Bitmap icon = iconGenerator.makeIcon();
        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(icon)).title(item.getTitle());
    }



    /*
    @Override
    public void setOnClusterItemInfoWindowClickListener(ClusterManager.OnClusterItemInfoWindowClickListener<ClusterMarker> listener) {

        Log.d("ClusterManager", "CLICKED INFO WINDOW");
    }

    public boolean onClusterItemClick(ClusterMarker item) {
        Log.d("ClusterManager", "CLICKED CLUSTER ITEM");

        return false;
    }*/




}
