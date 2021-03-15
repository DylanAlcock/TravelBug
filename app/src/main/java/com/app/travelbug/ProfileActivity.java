package com.app.travelbug;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.content.Intent;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ProfileActivity extends AppCompatActivity {


    private ProfileActivity activity = this;
    private TextView firstName;
    private TextView lastName;
    private TextView email;
    private TextView phone;
    private TextView userNameTextView;
    private String username;
    private ProgressBar progressBar;


    /**
     * Purpose:
     * Sets up the view when the profile activity is started
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);


        firstName = findViewById(R.id.firstnameTextView);
        lastName = findViewById(R.id.lastnameTextView);
        email = findViewById(R.id.emailTextView);
        phone = findViewById(R.id.phoneTextView);
        userNameTextView = findViewById(R.id.profileusernameTextView);
        progressBar = findViewById(R.id.ProgressBar1);
        progressBar.setVisibility(View.GONE);

        username = getIntent().getStringExtra("username");
        boolean myAccount = getIntent().getBooleanExtra("Check", true);


        final Button editButton = findViewById(R.id.editButton);
        final Button logOutButton = findViewById(R.id.logOutButton);

        if (myAccount) {
            editButton.setVisibility(View.VISIBLE);
            logOutButton.setVisibility(View.VISIBLE);
        }
        else {
            editButton.setVisibility(View.INVISIBLE);
            logOutButton.setVisibility(View.INVISIBLE);
        }


        /*userNameTextView.setText(username);
        firstName.setText(user.getFirstName());
        lastName.setText(user.getLastName());
        email.setText(user.getEmail());
        phone.setText(user.getPhoneNumber());*/

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
                    intent = new Intent(activity, FavoritesActivity.class);
                } else if (id == R.id.profile) {
                    return true;
                }
                startActivity(intent);
                return true;
            }
        });
        bottomNav.setSelectedItemId(R.id.profile);

       /* backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                finish();
            }
        });*/
        /*editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, EditProfileActivity.class);
                //Gson gson = new Gson();
                //intent.putExtra("user",gson.toJson(user));
                intent.putExtra("username",user.getUsername());
                startActivity(intent);
                finish();
            }
        });
        logOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                Intent intent = new Intent(activity, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });*/

    }
}