package com.app.travelbug;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.app.travelbug.data.model.ClusterMarker;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.maps.android.clustering.ClusterItem;

public class PlannerActivity extends AppCompatActivity {

    private PlannerActivity activity = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_planner);


        BottomNavigationView bottomNav = (BottomNavigationView) findViewById(R.id.bottomNav_id);

        bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected (@NonNull MenuItem item){
                int id = item.getItemId();
                Intent intent = null;
                if (id == R.id.map) {
                    intent = new Intent(activity, MapsActivity.class);
                } else if (id == R.id.planner) {
                    return true;
                } else if (id == R.id.favorites) {
                    intent = new Intent(activity, FavoritesActivity.class);
                } else if (id == R.id.profile) {
                    intent = new Intent(activity, ProfileActivity.class);
                }
                startActivity(intent);
                return true;
            }
        });
        bottomNav.setSelectedItemId(R.id.planner);

        //ClusterMarker marker = (ClusterMarker) getIntent().getSerializableExtra("ClusterItem");
        //TextView textView = (TextView) findViewById(R.id.textView2);
        //textView.setText(marker.getTitle());

        //Toast.makeText(PlannerActivity.this, marker.getTitle(), Toast.LENGTH_SHORT).show();

    }
}
