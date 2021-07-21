package com.app.travelbug;

import androidx.annotation.AnyRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import android.database.CursorWindow;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.lang.reflect.Field;

import com.app.travelbug.data.DatabaseHelper;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class TicketInfoActivity extends AppCompatActivity {

    GridView gridView;

    DatabaseHelper db;
    CustomAdapter customAdapter;

    private ArrayList<String> allImages;
    private ArrayList<String> allImageNames;
    Bitmap icon;

    String[] names = {"test name", "Add Photo"};
    int[] images = {R.drawable.ic_launcher_background, R.drawable.ic_add_planner};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_info);

        Activity activity = this;

        try {
            Field field = CursorWindow.class.getDeclaredField("sCursorWindowSize");
            field.setAccessible(true);
            field.set(null, 100 * 1024 * 1024); //the 100MB is the new size
        } catch (Exception e) {
            if (e != null) {
                e.printStackTrace();
            }
        }

        gridView = findViewById(R.id.gridView);
        BottomNavigationView bottomNav = (BottomNavigationView) findViewById(R.id.bottomNav_id);
        db = new DatabaseHelper(this);
        allImages = db.fetchTicketImages();
        allImageNames = db.fetchTicketNames();
        for (String member : allImages){
            Log.i("Member image: ", member);
        }
        for (String member : allImageNames){
            Log.i("Member name: ", member);
        }

        icon = BitmapFactory.decodeResource(getResources(),
                R.drawable.ic_add_white);

        Uri uri = Uri.parse("android.resource://com.app.travelbug/drawable/ic_add_white");
        try {
            InputStream stream = getContentResolver().openInputStream(uri);
            Toast.makeText(this, stream.toString(), Toast.LENGTH_SHORT).show();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        //allImages = new ArrayList<Bitmap>();
        allImages.add(uri.toString());
        allImageNames.add("Add Photo");

        if (allImageNames != null) {  // if the bitmaps were found
            //ImageGridViewAdapter adapter = new ImageGridViewAdapter(this, bitmaps, columnWidth);   // using custom adapter for showing images
            // columnWidth is just some int value representing image width
            //imageGridview.setAdapter(adapter);



            customAdapter = new CustomAdapter(allImageNames, allImages, this);
            gridView.setAdapter(customAdapter);
        }

        //CustomAdapter customAdapter = new CustomAdapter(allImageNames, allImages, this);
        //gridView.setAdapter(customAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == allImageNames.size() -1) {
                    Toast.makeText(TicketInfoActivity.this, "pos " + position + " size " + allImageNames.size() , Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(TicketInfoActivity.this, AddTicketInfo.class);
                    startActivity(intent);
                    return;
                }

                String selectedName = allImageNames.get(position);
                String selectedImage = allImages.get(position);

                Intent intent = new Intent(TicketInfoActivity.this, TicketImageClicked.class);
                intent.putExtra("name", selectedName).putExtra("image", selectedImage);
                startActivity(intent);
            }
        });


        gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View view, int position, long id) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity);
                alertDialogBuilder.setMessage("Do you want to delete?");
                        alertDialogBuilder.setPositiveButton("Yes",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface arg0, int arg1) {
                                        Toast.makeText(activity, "Long Press", Toast.LENGTH_SHORT).show();
                                        db.deleteTicket(allImageNames.get(position), allImages.get(position));
                                        allImageNames.remove(position);
                                        allImages.remove(position);
                                        customAdapter.updateResults(allImageNames,allImages);
                                        gridView.setAdapter(customAdapter);
                                    }
                                });

                alertDialogBuilder.setNegativeButton("No",null);

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();

                return true;
            }
        });


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

    }

    public class CustomAdapter extends BaseAdapter{

        private ArrayList<String> imageNames;
        private ArrayList<String> imagePhotos;
        private Context context;
        private LayoutInflater layoutInflater;


        public CustomAdapter(ArrayList<String> imageNames, ArrayList<String> imagesPhoto, Context context) {
            this.imageNames = imageNames;
            this.imagePhotos = imagesPhoto;
            this.context = context;
            this.layoutInflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
        }


        @Override
        public int getCount() {
            return imageNames.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View view, ViewGroup parent) {

            if(view == null){
                view = layoutInflater.inflate(R.layout.grid_items, parent, false);
            }

            TextView imageName= view.findViewById(R.id.gridImageName);
            ImageView imageView = view.findViewById(R.id.gridImage);


            imageName.setText(imageNames.get(position));
            //imageName.setText("FUCK");

            imageView.setImageURI(Uri.parse(imagePhotos.get(position)));
            //imageView.setImageResource(R.drawable.ic_add_white);
            //imageView.setImageBitmap(imagePhotos.get(position));
            //imageView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);

            return view;
        }


        public void updateResults(ArrayList<String> names, ArrayList<String> images) {
            imageNames = names;
            imagePhotos = images;
            //Triggers the list update
            notifyDataSetChanged();
        }
    }

}