package com.app.travelbug;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.app.travelbug.data.DatabaseHelper;
import com.app.travelbug.data.PlaceListAdapter;
import com.app.travelbug.data.model.ClusterMarker;
import com.app.travelbug.data.model.LoggedInUser;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class FavoritesActivity extends AppCompatActivity {

    private FavoritesActivity activity = this;
    DatabaseHelper mDatabaseHelper;

    private ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);

        mListView = (ListView) findViewById(R.id.favListView);
        mDatabaseHelper = new DatabaseHelper(this);

        BottomNavigationView bottomNav = (BottomNavigationView) findViewById(R.id.bottomNav_id);
        bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected (@NonNull MenuItem item){
                int id = item.getItemId();
                Intent intent = null;
                if (id == R.id.map) {
                    intent = new Intent(activity, MapsActivity.class);
                } else if (id == R.id.planner) {
                    intent = new Intent(activity, PlannerActivity.class);
                } else if (id == R.id.favorites) {
                    return true;
                } else if (id == R.id.profile) {
                    intent = new Intent(activity, ProfileActivity.class);
                }
                startActivity(intent);
                return true;
            }
        });
        bottomNav.setSelectedItemId(R.id.favorites);


        populateListView();
    }


    private void populateListView(){

        ArrayList<ClusterMarker> listData = mDatabaseHelper.getAllPlaces();

        //ListAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listData);
        ArrayAdapter<ClusterMarker> adapter = new PlaceListAdapter(this, 0, listData);
        mListView.setAdapter(adapter);
    }
}