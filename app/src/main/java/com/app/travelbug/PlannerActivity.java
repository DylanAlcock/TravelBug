package com.app.travelbug;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.app.travelbug.data.DatabaseHelper;
import com.app.travelbug.data.PlaceListAdapter;
import com.app.travelbug.data.model.ClusterMarker;
import com.app.travelbug.data.model.RecycleAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.Collections;

public class PlannerActivity extends AppCompatActivity {

    private PlannerActivity activity = this;
    private DatabaseHelper mDatabaseHelper;
    private RecyclerView recyclerView;
    private ListView listView;
    String planName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_planner);

        String tutorialsName = "Home";

        mDatabaseHelper = new DatabaseHelper(this);

        Button addPlanButton = findViewById(R.id.add_planner_button);
        Spinner dropdown = findViewById(R.id.info_spinner);

        BottomNavigationView bottomNav = (BottomNavigationView) findViewById(R.id.bottomNav_id);
        //recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        listView = (ListView) findViewById(R.id.recyclerView);

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


        addPlanButton.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                 builder.setTitle("Plan Name");

                 // Set up the input
                 final EditText input = new EditText(activity);
                 input.setInputType(InputType.TYPE_CLASS_TEXT);
                 builder.setView(input);

                 // Set up the buttons
                 builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                     @Override
                     public void onClick(DialogInterface dialog, int which) {
                         //Toast.makeText(activity, input.getText().toString(), Toast.LENGTH_SHORT).show();
                         mDatabaseHelper.createPlanner(input.getText().toString());
                     }
                 });
                 builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                     @Override
                     public void onClick(DialogInterface dialog, int which) {
                         dialog.cancel();
                     }
                 });

                 builder.show();
             }
        });



        ArrayList<String> arrayList = mDatabaseHelper.getPlans();
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, arrayList);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dropdown.setAdapter(arrayAdapter);
        dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                planName = parent.getItemAtPosition(position).toString();
                //Toast.makeText(parent.getContext(), "Selected: " + planName,          Toast.LENGTH_LONG).show();
                populateListView();
            }
            @Override
            public void onNothingSelected(AdapterView <?> parent) {
            }
        });



        //ClusterMarker marker = (ClusterMarker) getIntent().getSerializableExtra("ClusterItem");
        //TextView textView = (TextView) findViewById(R.id.textView2);
        //textView.setText(mDatabaseHelper.getItemsFromPlan(tutorialsName));

        //Toast.makeText(PlannerActivity.this, marker.getTitle(), Toast.LENGTH_SHORT).show();



    }

    private void populateListView(){

        Toast.makeText(activity, mDatabaseHelper.getItemsFromPlan(planName), Toast.LENGTH_SHORT).show();
        ArrayList<String> myList = new ArrayList();
        String[] listData = mDatabaseHelper.convertStringToArray(mDatabaseHelper.getItemsFromPlan(planName));
        if (listData != null){
            Collections.addAll(myList, listData);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, myList);

        //RecycleAdapter adapter = new RecycleAdapter(this, listData);
        //recyclerView.setAdapter((RecyclerView.Adapter) adapter);
        listView.setAdapter(adapter);
    }
}
