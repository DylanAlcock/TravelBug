package com.app.travelbug.data;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.travelbug.R;
import com.app.travelbug.data.model.ClusterMarker;
import com.google.maps.android.clustering.Cluster;

import java.util.ArrayList;
import java.util.List;


public class PlaceListAdapter extends ArrayAdapter<ClusterMarker> {
    private Context context;
    private List<ClusterMarker> places;

    public PlaceListAdapter (Context context, int resource, ArrayList<ClusterMarker> objects) {
        super(context, resource, objects);

        this.context = context;
        this.places = objects;
    }

    public View getView (int position, View convertView, ViewGroup parent) {
        ClusterMarker marker = places.get(position);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.custom_list_item, null);

        TextView  listTitle  = (TextView) view.findViewById(R.id.customListTitle);
        TextView  listSnippet = (TextView) view.findViewById(R.id.customListSnippet);
        ImageView listImage = (ImageView) view.findViewById(R.id.customListImage);


        listTitle.setText(marker.getTitle());
        listSnippet.setText(marker.getSnippet());
        listImage.setImageResource(marker.getIconPicture());

        return view;
    }


}
