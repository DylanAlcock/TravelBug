package com.app.travelbug.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.app.travelbug.R;
import com.app.travelbug.data.model.ClusterMarker;
import com.app.travelbug.data.model.LoggedInUser;
import com.app.travelbug.data.model.PlaceInfo;
import com.app.travelbug.data.model.UserFavorites;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.material.tabs.TabLayout;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.Array;
import java.security.cert.TrustAnchor;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String TAG = "DatabaseHelper";
    private static final String COL1 = "id";
    private static final String COL2 = "name";

    private static final String DATABASE_NAME = "usersManager";

    private static final String TABLE_USERS = "user_table";
    private static final String TABLE_PLANNER = "planner_table";
    private static final String TABLE_FAVORITES = "favorites_table";
    private static final String TABLE_PLACES = "place_table";
    private static final String TABLE_TICKETS = "tickets_table";


    //USER TABLE - column names
    private static final String KEY_EMAIL = "email";
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_FNAME = "first_name";
    private static final String KEY_LNAME = "last_name";
    private static final String KEY_PASSWORD = "password";

    //PLANNER TABLE - column names
    private static final String KEY_PLANNER_LIST = "planner_list_name";
    private static final String KEY_PLANNER_ITEMS = "planner_items";
    private static final String KEY_DISTANCE = "distance_items";

    //PLACES TABLE - column names
    private static final String KEY_PLACE_ID = "place_id";
    private static final String KEY_TITLE = "title";
    private static final String KEY_SNIPPET = "snippet";
    private static final String KEY_LAT = "lat";
    private static final String KEY_LNG = "lng";
    private static final String KEY_WEBSITE = "website";
    private static final String KEY_ADDRESS = "address";


    //TICKET TABLE - column names
    private static final String KEY_TICKET_TITLE = "ticket_title";
    private static final String KEY_TICKET_IMAGE = "ticket_image";




    // Table Create Statements
    // User table create statement
    private static final String CREATE_TABLE_USER = "CREATE TABLE "
            + TABLE_USERS + "(" + KEY_EMAIL + " TEXT PRIMARY KEY," + KEY_USER_ID + "INTEGER," + KEY_FNAME
            + " TEXT," + KEY_LNAME + " TEXT," + KEY_PASSWORD + " TEXT)";


    // Planner table create statement
    private static final String CREATE_TABLE_PLANNER = "CREATE TABLE " + TABLE_PLANNER
            + "(" + KEY_PLANNER_LIST + " TEXT PRIMARY KEY," + KEY_PLANNER_ITEMS + " TEXT)";


    //Favorites table create statement
    private static final String CREATE_TABLE_FAVORITES = "CREATE TABLE " + TABLE_FAVORITES
            + "(" + KEY_PLANNER_LIST + " TEXT PRIMARY KEY," + KEY_PLANNER_ITEMS + " TEXT," + KEY_DISTANCE + "INTEGER" + ")";

    // Places table create statement
    private static final String CREATE_TABLE_PLACES = "CREATE TABLE "
            + TABLE_PLACES + "(" + KEY_PLACE_ID + " INTEGER PRIMARY KEY," + KEY_TITLE
            + " TEXT," + KEY_SNIPPET+ " TEXT," + KEY_ADDRESS+ " TEXT," + KEY_WEBSITE+ " TEXT,"
            + KEY_LAT + " REAL," + KEY_LNG + " REAL)";


    //Tickets table create statement
    private static final String CREATE_TABLE_TICKETS = "CREATE TABLE " + TABLE_TICKETS
            + "(" + KEY_TICKET_TITLE + " TEXT PRIMARY KEY," + KEY_TICKET_IMAGE + " TEXT)";



    public static String strSeparator = "__,__";
    public static String convertArrayToString(String[] array){
        String str = "";
        for (int i = 0;i<array.length; i++) {
            str = str+array[i];
            // Do not append comma at the end of last element
            if(i<array.length-1){
                str = str+strSeparator;
            }
        }
        return str;
    }
    public static String[] convertStringToArray(String str){
        String[] arr;
        if (str !=null) {
            arr = str.split(strSeparator);
        }
        else{
            arr = null;
        }
        return arr;
    }



    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 2);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_USER);
        db.execSQL(CREATE_TABLE_PLANNER);
        db.execSQL(CREATE_TABLE_FAVORITES);
        db.execSQL(CREATE_TABLE_PLACES);
        db.execSQL(CREATE_TABLE_TICKETS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // on upgrade drop older tables
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PLANNER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FAVORITES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PLACES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TICKETS);

        onCreate(db);
    }


    public boolean addData(String item){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL2, item);

        Log.d(TAG, "addData: Adding " + item + " to " + TABLE_USERS);

        long result = db.insert(TABLE_USERS, null, contentValues);

        //check if data was inserted correctly
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }


    public Cursor getData(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_USERS;
        Cursor data = db.rawQuery(query, null);
        return data;
    }



    //*****  ALL USER DB OPERATIONS *****//

    public long createUser(LoggedInUser user) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(KEY_FNAME, user.getFirstName());
        values.put(KEY_LNAME, user.getLastName());
        values.put(KEY_EMAIL, user.getEmail());
        values.put(KEY_PASSWORD, user.getPassword());

        // insert row
        long user_id = db.insert(TABLE_USERS, null, values);

        // assigning tags to todo
        //for (long tag_id : tag_ids) {
        //    createTodoTag(todo_id, tag_id);
        //}

        return user_id;
    }

    public ArrayList<String> getAllUsers() {
        ArrayList<String> users = new ArrayList<String>();

        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_USERS;

        Log.e(TAG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        if (c.moveToFirst()) {
            do {
                users.add((c.getString(c.getColumnIndex(KEY_EMAIL))));
            } while (c.moveToNext());
        }

        return users;
    }

    public LoggedInUser getUserByEmail(String userEmail) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_USERS + " WHERE "
                + KEY_EMAIL + " = " + userEmail;

        Log.e(TAG, selectQuery);

        Cursor c = db.rawQuery("SELECT  * FROM " + TABLE_USERS + " WHERE "
                + KEY_EMAIL + " = ?", new String[]{userEmail});

        if (c != null)
            c.moveToFirst();
        else {
            return null;
        }

        LoggedInUser td = new LoggedInUser();
        td.setEmail(c.getString(c.getColumnIndex(KEY_EMAIL)));
        td.setFirstName((c.getString(c.getColumnIndex(KEY_FNAME))));
        td.setLastName((c.getString(c.getColumnIndex(KEY_LNAME))));

        return td;
    }


    /*
     * Updating a User
     */
    public int updateToDo(LoggedInUser user) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_USER_ID, user.getUserId());
        values.put(KEY_EMAIL, user.getEmail());

        // updating row
        return db.update(TABLE_USERS, values, KEY_USER_ID + " = ?",
                new String[] { String.valueOf(user.getUserId()) });
    }


    /*
     * Deleting a user
     */
    public void deleteUser(String userEmail) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_USERS, KEY_EMAIL + " = ?",
                new String[]{userEmail});
    }



    //*****  ALL Planner DB OPERATIONS *****//

    public void createPlanner(String plannerName) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(KEY_PLANNER_LIST, plannerName);

        // insert row
        db.insert(TABLE_PLANNER, null, values);

    }



    public ArrayList<String> getPlans() {
        SQLiteDatabase db = this.getReadableDatabase();

        ArrayList<String> plannerList = new ArrayList<String>();
        String dbString = "";

        String selectQuery = "SELECT " + KEY_PLANNER_LIST + " FROM " + TABLE_PLANNER + " WHERE 1";

        Log.e(TAG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        if (c.moveToFirst()) {
            do {
                plannerList.add(c.getString(c.getColumnIndex(KEY_PLANNER_LIST)));
            } while (c.moveToNext());
        }

        return plannerList;
    }





    public String getItemsFromPlan(String planName){
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor c = db.rawQuery("SELECT  * FROM " + TABLE_PLANNER + " WHERE "
                + KEY_PLANNER_LIST + " = ?", new String[]{planName});

        if (c != null)
            c.moveToFirst();
        else {
            return null;
        }

        return (c.getString(c.getColumnIndex(KEY_PLANNER_ITEMS)));
    }


    // Add item to planner
    public int addItemToPlan(String planName, String item) {
        SQLiteDatabase db = this.getReadableDatabase();

        String planItems = getItemsFromPlan(planName);

        if (planItems != null){
            planItems = planItems + strSeparator +item;
        } else {
            planItems = item;
        }

        ContentValues values = new ContentValues();
        values.put(KEY_PLANNER_ITEMS, planItems);
        return db.update(TABLE_PLANNER, values, KEY_PLANNER_LIST + " = '" + planName + "'", null);

    }



    // Deleting a plan
    public void deletePlan(String planName) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_PLANNER, KEY_PLANNER_LIST + " = ?",
                new String[] {planName});
    }




    //*****  ALL FAVORITES DB OPERATIONS *****//

    public long createFavorite(PlaceInfo place) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(KEY_TITLE, place.getTitle());
        values.put(KEY_SNIPPET, place.getSnippet());
        values.put(KEY_ADDRESS, place.getAddress());
        values.put(KEY_WEBSITE, place.getWebsite());
        values.put(KEY_LAT, place.getLat());
        values.put(KEY_LNG, place.getLng());

        // @TODO NEED TO SAVE IMAGES INTO DATABASE

        // insert row
        long user_id = db.insert(TABLE_PLACES, null, values);

        // assigning tags to todo
        //for (long tag_id : tag_ids) {
        //    createTodoTag(todo_id, tag_id);
        //}

        return user_id;
    }



    public ArrayList<Integer> getFavoriteByUserEmail(String userEmail) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_FAVORITES + " WHERE "
                + KEY_EMAIL + " = " + userEmail;

        Log.e(TAG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null) {
            c.moveToFirst();
        } else {
            return null;
        }

        ArrayList<Integer> placeList = new ArrayList<Integer>();
        while(c.getString(c.getColumnIndex(KEY_EMAIL)) == userEmail){
            placeList.add(c.getColumnIndex(KEY_PLACE_ID));
        }

        return placeList;
    }



    // Deleting a favorite
    public void deleteFavorite(String userEmail, long place_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_FAVORITES, KEY_EMAIL + " = ?" + " AND " + KEY_PLACE_ID + " = ?" ,
                new String[] {userEmail, String.valueOf(place_id) });
    }




    //*****  ALL PLACES DB OPERATIONS *****//

    public long createPlace(ClusterMarker place) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(KEY_TITLE, place.getTitle());
        values.put(KEY_SNIPPET, place.getSnippet());
        values.put(KEY_ADDRESS, "NONE");
        values.put(KEY_WEBSITE, "NONE");
        //values.put(KEY_ADDRESS, place.getAddress());
        //values.put(KEY_WEBSITE, place.getWebsite());
        values.put(KEY_LAT, place.getLat());
        values.put(KEY_LNG, place.getLng());


        // insert row
        long user_id = db.insert(TABLE_PLACES, null, values);

        // assigning tags to todo
        //for (long tag_id : tag_ids) {
        //    createTodoTag(todo_id, tag_id);
        //}

        return user_id;
    }



    public PlaceInfo getPlaceByID(long place_id) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_PLACES + " WHERE "
                + KEY_PLACE_ID + " = " + place_id;

        Log.e(TAG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null) {
            c.moveToFirst();
        } else {
           return null;
        }

        PlaceInfo place = new PlaceInfo(1, c.getString(c.getColumnIndex(KEY_TITLE)),
                c.getString(c.getColumnIndex(KEY_SNIPPET)),
                new LatLng(c.getDouble(c.getColumnIndex(KEY_LAT)), c.getDouble(c.getColumnIndex(KEY_LNG))),
                c.getString(c.getColumnIndex(KEY_ADDRESS)),
                c.getString(c.getColumnIndex(KEY_WEBSITE)),
                "$$$");

        return place;
    }



    public ArrayList<ClusterMarker> getAllPlaces() {
        ArrayList<ClusterMarker> places = new ArrayList<ClusterMarker>();

        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_PLACES;

        Log.e(TAG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        if (c.moveToFirst()) {
            do {
                ClusterMarker marker = new ClusterMarker(
                        new LatLng(c.getDouble(c.getColumnIndex(KEY_LAT)), c.getDouble(c.getColumnIndex(KEY_LNG))),
                        c.getString(c.getColumnIndex(KEY_TITLE)),
                        c.getString(c.getColumnIndex(KEY_SNIPPET)),
                        R.drawable.cathedral_of_toledo);
                places.add(marker);
            } while (c.moveToNext());
        }

        return places;
    }


    // Deleting a place
    public void deletePlace(long place_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_PLACES, KEY_PLACE_ID + " = ?",
                new String[] { String.valueOf(place_id) });
    }



    //
    public void addTicket(String name, String image) throws SQLiteException {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new  ContentValues();
        cv.put(KEY_TICKET_TITLE,    name);
        cv.put(KEY_TICKET_IMAGE,   image);
        db.insert( TABLE_TICKETS, null, cv);
    }

    public void deleteTicket(String name, String image){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_TICKETS, KEY_TICKET_TITLE + " == ? AND " + KEY_TICKET_IMAGE + " == ?" , new String[] {name, image});
    }

    //----------Old method when saving bitmap of image
    /*public ArrayList<Bitmap> fetchTicketImages(){
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT " + KEY_TICKET_IMAGE + " FROM " + TABLE_TICKETS ;
        Log.e(TAG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        Bitmap bitmap;
        ArrayList<Bitmap> bitmaps = new ArrayList<Bitmap>();

        if (c.moveToFirst()) {
            do {
                byte[] blob = c.getBlob(c.getColumnIndex(KEY_TICKET_IMAGE));
                bitmap = getImage(blob);
                bitmaps.add(bitmap);
            } while (c.moveToNext());
        }

        return bitmaps;
    }*/

    public ArrayList<String> fetchTicketImages(){
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT " + KEY_TICKET_IMAGE + " FROM " + TABLE_TICKETS + " ORDER BY ROWID";
        Log.e(TAG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);
        ArrayList<String> images = new ArrayList<String>();


        if (c.moveToFirst()) {
            do {
                images.add(c.getString(c.getColumnIndex(KEY_TICKET_IMAGE)));
            } while (c.moveToNext());
        }

        return images;
    }


    public ArrayList<String> fetchTicketNames(){
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT " + KEY_TICKET_TITLE + " FROM " + TABLE_TICKETS + " ORDER BY ROWID";
        Log.e(TAG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);
        ArrayList<String> names = new ArrayList<String>();


        if (c.moveToFirst()) {
            do {
                Log.e(TAG, c.getString(c.getColumnIndex(KEY_TICKET_TITLE)));
                names.add(c.getString(c.getColumnIndex(KEY_TICKET_TITLE)));
            } while (c.moveToNext());
        }


        return names;
    }


    // convert from bitmap to byte array
    public static byte[] getBytes(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream);
        return stream.toByteArray();
    }

    // convert from byte array to bitmap
    public static Bitmap getImage(byte[] image) {
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }


    // closing database
    public void closeDB() {
        SQLiteDatabase db = this.getReadableDatabase();
        if (db != null && db.isOpen())
            db.close();
    }
}
