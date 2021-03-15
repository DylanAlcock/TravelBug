package com.app.travelbug;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class FavoritesActivity extends AppCompatActivity {

    private FavoritesActivity activity = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);

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
    }
}